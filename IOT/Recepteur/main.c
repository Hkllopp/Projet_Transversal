#include "core/system.h"
#include "core/systick.h"
#include "core/pio.h"
#include "lib/stdio.h"
#include "drivers/serial.h"
#include "drivers/gpio.h"
#include "drivers/ssp.h"
#include "extdrv/cc1101.h"
#include "extdrv/status_led.h"
#include "drivers/i2c.h"

// AES
#define ECB 1
#include "aes.h"
struct AES_ctx ctx;
uint8_t key[16] = { (uint8_t) 0x2b, (uint8_t) 0x7e, (uint8_t) 0x15, (uint8_t) 0x16, (uint8_t) 0x28, (uint8_t) 0xae, (uint8_t) 0xd2, (uint8_t) 0xa6, (uint8_t) 0xab, (uint8_t) 0xf7, (uint8_t) 0x15, (uint8_t) 0x88, (uint8_t) 0x09, (uint8_t) 0xcf, (uint8_t) 0x4f, (uint8_t) 0x3c };

// Module
#define MODULE_VERSION  0x03
#define MODULE_NAME "RF Sub1G - USB"

// RF
#define RF_868MHz  1
#define RF_915MHz  0
#if ((RF_868MHz) + (RF_915MHz) != 1)
#error Either RF_868MHz or RF_915MHz MUST be defined.
#endif

#define RF_BUFF_LEN  32 // taille du buffer

#define SELECTED_FREQ  FREQ_SEL_48MHz
#define DEVICE_ADDRESS  0xBA /* Addresses 0x00 and 0xFF are broadcast */
#define NEIGHBOR_ADDRESS 0xAB /* Address of the associated device */

static volatile uint8_t buffer_receive[RF_BUFF_LEN] = ""; // buffer de reception
static volatile uint8_t buffer_send[RF_BUFF_LEN] = ""; // buffer d'envoi
volatile uint8_t buffer[1024] = ""; //buffer
int ptr = 0; // permet de refragmenter un message envoyé par radio
int fin = 0;

static volatile uint32_t cc_tx = 0; // variable pour déclencher une transmition radio
static volatile uint8_t cc_ptr = 0; // variable pour remplir le buffer

/***************************************************************************** */
/* Pins configuration */
const struct pio_config common_pins[] = {
    /* UART 0 */
    { LPC_UART0_RX_PIO_0_1,  LPC_IO_DIGITAL },
    { LPC_UART0_TX_PIO_0_2,  LPC_IO_DIGITAL },
    /* SPI */
    { LPC_SSP0_SCLK_PIO_0_14, LPC_IO_DIGITAL },
    { LPC_SSP0_MOSI_PIO_0_17, LPC_IO_DIGITAL },
    { LPC_SSP0_MISO_PIO_0_16, LPC_IO_DIGITAL },
    /* I2C 0 */
    { LPC_I2C0_SCL_PIO_0_10, (LPC_IO_DIGITAL | LPC_IO_OPEN_DRAIN_ENABLE) },
    { LPC_I2C0_SDA_PIO_0_11, (LPC_IO_DIGITAL | LPC_IO_OPEN_DRAIN_ENABLE) },
    ARRAY_LAST_PIO,
};

const struct pio cc1101_cs_pin = LPC_GPIO_0_15;
const struct pio cc1101_miso_pin = LPC_SSP0_MISO_PIO_0_16;
const struct pio cc1101_gdo0 = LPC_GPIO_0_6;
const struct pio cc1101_gdo2 = LPC_GPIO_0_7;

const struct pio status_led_green = LPC_GPIO_0_28;
const struct pio status_led_red = LPC_GPIO_0_29;

const struct pio button = LPC_GPIO_0_12; /* ISP button */

char checksum(char* s) // Modular sum (CRC)
{
	signed char sum = -1;
	while (*s != 0)
	{
		sum += *s;
		s++;
	}
	return sum;
}

void handle_uart_cmd(uint8_t c) // Fonction lecture lisaison UART + remplissage variable buffer_send (utilisation ultérieure)
{
    if (cc_ptr < RF_BUFF_LEN) {
        buffer_send[cc_ptr++] = c;
    } else {
        cc_ptr = 0;
    }
    if ((c == '\n') || (c == '\r')) {
        cc_tx = 1;
    }

}

/***************************************************************************** */
void system_init()
{
    startup_watchdog_disable();
    system_set_default_power_state();
    clock_config(SELECTED_FREQ);
    set_pins(common_pins);
    gpio_on();
    systick_timer_on(1);
    systick_start();
}

/* Define our fault handler. This one is not mandatory, the dummy fault handler
 * will be used when it's not overridden here.
 * Note : The default one does a simple infinite loop. If the watchdog is deactivated
 * the system will hang.
 */
void fault_info(const char* name, uint32_t len)
{
    uprintf(UART0, name);
    while (1);
}

static volatile int check_rx = 0;
void rf_rx_calback(uint32_t gpio)
{
    check_rx = 1;
}

static uint8_t rf_specific_settings[] = {
    CC1101_REGS(gdo_config[2]), 0x07, /* GDO_0 - Assert on CRC OK | Disable temp sensor */
    CC1101_REGS(gdo_config[0]), 0x2E, /* GDO_2 - FIXME : do something usefull with it for tests */
    CC1101_REGS(pkt_ctrl[0]), 0x0F, /* Accept all sync, CRC err auto flush, Append, Addr check and Bcast */
};

/* RF config */
void rf_config(void)
{
    config_gpio(&cc1101_gdo0, LPC_IO_MODE_PULL_UP, GPIO_DIR_IN, 0);
    cc1101_init(0, &cc1101_cs_pin, &cc1101_miso_pin); /* ssp_num, cs_pin, miso_pin */
    /* Set default config */
    cc1101_config();
    /* And change application specific settings */
    cc1101_update_config(rf_specific_settings, sizeof(rf_specific_settings));
    set_gpio_callback(rf_rx_calback, &cc1101_gdo0, EDGE_RISING);
    cc1101_set_address(DEVICE_ADDRESS);
}

void handle_rf_rx_data(void)
{
    uint8_t data[RF_BUFF_LEN+4];
    int8_t ret = 0;
    uint8_t status = 0;
    
    AES_init_ctx(&ctx,key);

    /* Check for received packet (and get it if any) */
    ret = cc1101_receive_packet(data, sizeof(data), &status);

   	cc1101_enter_rx_mode();
   	//check de l'adresse source
   	if (data[34] == NEIGHBOR_ADDRESS)
   	{
   		//copie du payload chiffré en mémoire
   		memcpy(&buffer_receive,&data[2],RF_BUFF_LEN);

   		//déchiffrement AES et stockage dans buffer_receive
    	for (int i = 0; i < 2; ++i)
    	{
    		AES_ECB_decrypt(&ctx,buffer_receive+(i*16));
    	}
    	//calcul du checksum 
    	if (checksum(buffer_receive)==data[35])
    	{
    		//uprintf(UART0, "%s", buffer_receive); // affichage des fragments
            for(int i = 0; i<strlen(buffer_receive); i++){ // on refragmente le message
                if(buffer_receive[i] != '\n'){
                    buffer[i+ptr] = buffer_receive[i];
                }
                else{
                    buffer[i+ptr] = '\n';
                    for(int j=0; j<strlen(buffer);j++){ // on envoie le message entier sur la liaison UART
                        uprintf(UART0, "%c",buffer[j]);
                    }
                    ptr=0;
                    fin = 1;
                    for(int k=0; k<sizeof(buffer);k++){
					    buffer[k]=0;
				    }
                    break;
                }
            }
            if(fin == 1){
                fin = 0;
            }
            else{
                ptr += 32;
            }
    	}
    	
   	}
}

void send_on_rf(void) //permet d'envoyer des données en radio (utilisation ultérieure)
{
	//uprintf(UART0, "On rentre dans send_on_rf : \n\r");
    uint8_t cc_tx_data[sizeof(buffer_send)+2];
    cc_tx_data[0]=sizeof(buffer_send)+1;
    cc_tx_data[1]=NEIGHBOR_ADDRESS;
    memcpy(&cc_tx_data[2], &buffer_send, sizeof(buffer_send));

    /* Send */
    if (cc1101_tx_fifo_state() != 0) {
        cc1101_flush_tx_fifo();
    }
 
    int ret = cc1101_send_packet(cc_tx_data, sizeof(buffer_send)+2);
}

int main(void)
{
    system_init();
    uart_on(UART0, 115200, handle_uart_cmd);
    ssp_master_on(0, LPC_SSP_FRAME_SPI, 8, 4*1000*1000); /* bus_num, frame_type, data_width, rate */
    status_led_config(&status_led_green, &status_led_red);
    
    
    /* Radio */
    rf_config();


    //uprintf(UART0, "App started\n\r");

    while (1) {
        uint8_t status = 0;

        
        /* Tell we are alive  */
        chenillard(250);
        
        /* RF */
        if (cc_tx == 1) { //envoi d'un message (utilisation ultérieure)
            send_on_rf();
            //uprintf(UART0, " send_on_rf execution OK \n\r");
            cc_tx = 0;
            for(int i=0; i<RF_BUFF_LEN;i++){
                buffer_send[i]=0;
            }
            cc_ptr = 0;
        }

        /* Do not leave radio in an unknown or unwated state */
        do {
            status = (cc1101_read_status() & CC1101_STATE_MASK);
        } while (status == CC1101_STATE_TX);

        if (status != CC1101_STATE_RX) {
            static uint8_t loop = 0;
            loop++;
            if (loop > 10) {
                if (cc1101_rx_fifo_state() != 0) {
                    cc1101_flush_rx_fifo();
                }
                cc1101_enter_rx_mode();
                loop = 0;
            }
        }
        if (check_rx == 1) {
            check_rx = 0;
            handle_rf_rx_data();
        }
    }
    return 0;
}

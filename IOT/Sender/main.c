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
#include "lib/string.h"
#include "lib/stdint.h"
#include "lib/stdlib.h"
#define ECB 1
#include "aes.h"
// ajouter un ack et fragmentation

// AES
struct AES_ctx ctx;

#define MODULE_VERSION	0x03
#define MODULE_NAME "RF Sub1G - USB"

#define RF_868MHz  1
#define RF_915MHz  0
#if ((RF_868MHz) + (RF_915MHz) != 1)
#error Either RF_868MHz or RF_915MHz MUST be defined.
#endif

//#define DEBUG 1
#define BUFF_LEN 60
#define RF_BUFF_LEN  32 // taille du buffer

#define SELECTED_FREQ  FREQ_SEL_48MHz
#define DEVICE_ADDRESS  0xAB /* Addresses 0x00 and 0xFF are broadcast */
#define NEIGHBOR_ADDRESS 0xBA /* Address of the associated device */

// Fonctions chiffrement
uint8_t key[16] ={ (uint8_t) 0x2b, (uint8_t) 0x7e, (uint8_t) 0x15, (uint8_t) 0x16, (uint8_t) 0x28, (uint8_t) 0xae, (uint8_t) 0xd2, (uint8_t) 0xa6, (uint8_t) 0xab, (uint8_t) 0xf7, (uint8_t) 0x15, (uint8_t) 0x88, (uint8_t) 0x09, (uint8_t) 0xcf, (uint8_t) 0x4f, (uint8_t) 0x3c };

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

static volatile uint8_t buffer_receive[RF_BUFF_LEN];
volatile uint8_t buffer_send[RF_BUFF_LEN] = "";

static volatile uint32_t cc_tx = 0;
static volatile uint8_t cc_ptr = 0;
void handle_uart_cmd(uint8_t c)
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

//Modular sum
char checksum(char* s)
{
	signed char sum = -1;
	while (*s != 0)
	{
		sum += *s;
		s++;
	}
	return sum;
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
#ifdef DEBUG
	uprintf(UART0, "CC1101 RF link init done.\n\r");
#endif
}


void handle_rf_rx_data(void)
{
	uint8_t data[RF_BUFF_LEN];
	int8_t ret = 0;
	uint8_t status = 0;

	/* Check for received packet (and get it if any) */
	ret = cc1101_receive_packet(data, RF_BUFF_LEN, &status);
	/* Go back to RX mode */
	cc1101_enter_rx_mode();
	memcpy(&buffer_receive,&data[2],sizeof(buffer_receive));
#ifdef DEBUG
    uprintf(UART0, "RF: message recu: %s.\n\r", (char*)&data[2]);
#endif
}

void send_on_rf(void)
{
	uint8_t cc_tx_data[sizeof(buffer_send)+4];

	cc_tx_data[0]=sizeof(buffer_send)+3;
	cc_tx_data[1]=NEIGHBOR_ADDRESS;
	cc_tx_data[34]=DEVICE_ADDRESS;
	cc_tx_data[35]=checksum(buffer_send); //CRC

	/*uprintf(UART0, "MSG: message a envoyer: %s\n\r", (char*)&buffer_send);
	uprintf(UART0, "MSG: CRC message: %d\n\r", cc_tx_data[35]);
	uprintf(UART0, "MSG: NEIGHBOR ADDRESS: %X\n\r", cc_tx_data[1]);
	uprintf(UART0, "MSG: DEVICE ADDRESS: %X\n\r", cc_tx_data[34]);
	uprintf(UART0, "MSG: Size: %d\n\r", cc_tx_data[0]);*/

	//Chiffrement
	uint8_t i;
	for (i = 0; i < 2; ++i)
    {
      AES_ECB_encrypt(&ctx, buffer_send + (i * 16));
    }
	memcpy(&cc_tx_data[2],&buffer_send, sizeof(buffer_send));

	/* Send */
	if (cc1101_tx_fifo_state() != 0) {
		cc1101_flush_tx_fifo();
	}

	int ret = cc1101_send_packet(cc_tx_data, sizeof(cc_tx_data));

#ifdef DEBUG
    uprintf(UART0, "RF: message envoyé: %s.\n\r", (char*)&cc_tx_data[2]);
#endif
}

int main(void)
{
	system_init();
	uart_on(UART0, 115200, handle_uart_cmd);
	//i2c_on(I2C0, I2C_CLK_100KHz, I2C_MASTER);
	ssp_master_on(0, LPC_SSP_FRAME_SPI, 8, 4*1000*1000); /* bus_num, frame_type, data_width, rate */
	status_led_config(&status_led_green, &status_led_red);
	
	/* Radio */
	rf_config();

	uprintf(UART0, "App started\n\r");

	while (1) {
		uint8_t status = 0;

		/* Tell we are alive :) */
		chenillard(250);
		
			/*AES*/
    		AES_init_ctx(&ctx, key);

		/* RF */
		if (cc_tx == 1) {
			#ifdef DEBUG
				uprintf(UART0, "Message a envoyer : %s\n\r", buffer_send);
			#endif
			send_on_rf();
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

import requests, json, time, serial

ip = '192.168.43.117'
port = '5001'
url = 'http://' + ip + ':' + port + '/todo/api/v1.0/getfire'
refresh = 2
buffer = ""

SERIALPORT = "/dev/ttyUSB0"
BAUDRATE = 115200
ser = serial.Serial()

def sendUARTMessage(msg):
    ser.write(msg.encode())

def initUART():     
    ser.port=SERIALPORT
    ser.baudrate=BAUDRATE
    ser.bytesize = serial.EIGHTBITS
    ser.parity = serial.PARITY_NONE
    ser.stopbits = serial.STOPBITS_ONE
    ser.timeout = None

    ser.xonxoff = False
    ser.rtscts = False
    ser.dsrdtr = False
    try:
        ser.open()
    except serial.SerialException:
        print("Serial {} port not available".format(SERIALPORT))
        exit()

initUART()

while(1):
    r = requests.get(url)
    data = r.json()

    for i in data:
        buffer += "(" + str(i["coordxfeu"]) + "," + str(i["coordyfeu"]) + "," + str(i["intensitefeu"]) + ")"

    buffer += '\n'
    print(buffer)
    sendUARTMessage(buffer)
    buffer=""
    time.sleep(refresh)

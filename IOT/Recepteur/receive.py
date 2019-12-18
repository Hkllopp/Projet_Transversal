import serial,re,json

from parse import *


ser = serial.Serial("/dev/ttyUSB0", 115200)


def parser(test_str):
    regex = r"\([0-9]\,[0-9]\,[0-9]\)"
    matches = re.finditer(regex, test_str, re.MULTILINE)

    for matchNum, match in enumerate(matches, start=1):
    
        #print ("Match {matchNum} was found at {start}-{end}: {match}".format(matchNum = matchNum, start = match.start(), end = match.end(), match = match.group()))
        print("{match}")
        for groupNum in range(0, len(match.groups())):
            groupNum = groupNum + 1
        
            #print ("Group {groupNum} found at {start}-{end}: {group}".format(groupNum = groupNum, start = match.start(groupNum), end = match.end(groupNum), group = match.group(groupNum)))


while True:
     cc=str(ser.readline())
     #print(cc)
     parser(cc)



from scapy.all import *
import codecs
import re

'''
capture = rdpcap('hash-browns.pcap')

output = open('output.txt','w')
for packet in capture:
    # if packet is ICMP and type is 8 (echo request)
    payload=bytes(packet[TCP].payload)
    if payload!=b'':
        print(payload)
        output.write(payload.decode('unicode_escape')+'\n')
'''
hashes={}
with open('cracked.txt','r') as f:
    lines=f.readlines()
    for line in lines:
        line=line.strip()
        if(line!=''):
            hash=line.split(':')
            hashes[hash[0]]=int(hash[1])

png1=[]
png2=[]
png3=[]           
#hashes= open('hash.txt', 'w')
with open('output.txt','r') as f:
    lines=f.readlines()
    for line in lines:
        byte=re.findall(r'Byte Matched=(.*)',line)
        if(len(byte)==0):
            byte=re.findall(r'ERROR!Received byte hash=(.*?),Original byte hash=(.*)',line)
            recv,og=byte[0]
            png2.append(hashes[recv])
            png3.append(hashes[og])
            #hashes.write(recv)
            #hashes.write('\n')
            #hashes.write(og)
            #hashes.write('\n')
        else:
            png1.append(int(byte[0]))
            png2.append(int(byte[0]))
            png3.append(int(byte[0]))

img1 = open('img1','wb')
img2 = open('img2','wb')
img3 = open('img3','wb')

img1.write(bytearray(png1))
img2.write(bytearray(png2))
img3.write(bytearray(png3))

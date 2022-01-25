from PIL import Image
import struct
import itertools
import random
import qrtools

qr = qrtools.QR()

def split(word):
    return [int(char) for char in word]

lines=""
bits=""
j=0

tested=[]
with open('test.txt','r') as f:
    lines=f.readlines()
    print(lines)

tmp=[]
for line in lines:
    line=line.strip()
    tmp.append(line)

lines=tmp
while True:
    random.shuffle(lines)
    while lines in tested:
        random.shuffle(lines)
    tested.append(list(lines))
    bits=""
    for line in lines:
        bits=bits+line

    bits=split(bits)

    size = 31,31
    arr = bits
    data = struct.pack('B'*len(arr), *[pixel*255 for pixel in arr])
    img = Image.frombuffer('L', size, data)
    img.save('image.png')
    qr.decode("image.png")
    print(qr.data)
    if(qr.data!="NULL"):
        break
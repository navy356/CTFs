from PIL import Image
import struct

def split(word):
    return [int(char) for char in word]

lines=""
bits=""
j=0
fp=open('qrpad.txt','w')
with open('qr1.txt','r') as f:
    lines=f.readlines()
    l=0
    for line in lines:
        line=line.strip()
        org=l
        l=l+len(line)
        print(l)
        if l==440:
            l=0
            j+=1
            bits=bits+line
            fp.write(line+'\n')
        elif l>440:
            if (l==0):
                offset=l-440
            else:
                offset = 440-org
            bits=bits+line[0:offset]
            j+=1
            fp.write(line[0:offset]+'\n')
            line=line[offset:]
            bits=bits+line
            fp.write(line)
            l=l-440
        else:
            bits=bits+line
            fp.write(line)

    if(l<440):
        j+=1
    while(l<440):
        fp.write('0')
        l+=1
    
    fp.write('\n')

while(j<440):
    line = '0'*440
    fp.write(line+'\n')
    bits=bits+line
    j+=1
fp.close()

bits=split(bits)

size = 440, 440
arr = bits
data = struct.pack('B'*len(arr), *[pixel*255 for pixel in arr])
img = Image.frombuffer('L', size, data)
img.save('image.png')
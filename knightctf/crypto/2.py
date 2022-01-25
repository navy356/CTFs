import os
import string
key = open('key.pem','r').read()

for i in range(1,256):
    i = chr(i)
    if i not in string.ascii_letters and i not in string.digits and i!='+' and i!='/' and i!='=':
        continue
    val = ord(i)

    keynew = key.replace('x',i)

    with open(f'key2/key{val}','w') as fp:
        fp.write(keynew)


    os.system(f'openssl rsautl -decrypt -in ./flag.enc -out ./key3/key{val}  -inkey ./key2/key{val}')
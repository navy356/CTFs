import string
result = "08'5[Z'Y:H3?X2K3V)?D2G3?H,N6?G$R(G]"

def decrypt():
    res = [chr(ord(i)+32) for i in result]
    flag = []
    for i in res:
        val = ord(i)
        if chr((-101-val)&0xff) in string.ascii_uppercase:
            flag.append(chr((-101-val)&0xff))
        elif chr((-37-val)&0xff) in string.ascii_lowercase:
            flag.append(chr((-37-val)&0xff))
        else:
            flag.append(i)

    print(''.join(flag))


decrypt()
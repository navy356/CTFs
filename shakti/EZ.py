a = 'lcZdl_Yoati+Xjn,lN!gGRdNR-R]H`=XjN,lo*+Iv'
b = list(a)
for i in range(len(a)):
    if 6 < i and i<0x11:
        pass
    if -1 < i and i < 4:
        pass
    if 3 < i and i < 7:
        b[i] = chr(ord(b[i]) + 3)
    if i < 0x1e and 0x10 < i:
        b[i] = chr(ord(b[i]) ^ 4)
    else:
        b[i] = chr(ord(b[i]) - 5)

print(''.join(b))

for i in range(len(a)):
    b[i] = chr((ord(b[i])+5)^1)

print(''.join(b))

for i in range(len(a)):
    if i < 0x14 or 0x1d < i:
        b[i] = chr(ord(b[i]) + 6)
    else:
        b[i] = chr(ord(b[i]) + 5)

print(''.join(b))

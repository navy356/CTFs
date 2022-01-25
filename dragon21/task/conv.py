a = "2c0000000f0a0000000000000a2000000000bb75080001000000bb75080002000000200a080003006e667400"

def convert(st):
    a = ''
    for i in range(0,len(st), 2):
        b = '\\x' + st[i:i+2]
        a += b
    return a
print(convert(a))

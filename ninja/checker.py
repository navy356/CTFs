import binascii

def up(x):
    x = [f"{ord(x[i]) << 1:08b}" for i in range(len(x))]
    return ''.join(x)

def down(x):
    x = ''.join(['1' if x[i] == '0' else '0' for i in range(len(x))])
    return x

def right(x,d):
    x = x[d:] + x[0:d]
    return x

def left(x,d):
    x = right(x,len(x)-d)
    return x[::-1]

def right_d(x,d):
    x = x[-d:]+x[0:-d]
    return x

def left_d(x,d):
    x = right_d(x[::-1],len(x)-d)
    return x

def down_d(x):
    x = ''.join(['1' if x[i] == '0' else '0' for i in range(len(x))])
    return x

def ascii(x):
    n = int('0b'+x, 2)
    x=binascii.unhexlify('%x' % n)
    return x

def up_d(x):
    y=[]
    for i in range(len(x)):
        if i%8==0:
            ch=ascii(x[i:i+8])
            y.append(chr(ord(ch) >> 1))
    #x = [f"{ord(x[i]) << 1:08b}" for i in range(len(x))]
    #return ''.join(x)
    return ''.join(y)

def encode(plain):
    d = 24
    x = up(plain)
    #print(f"Encode up {x}")
    x = right(x,d)
    #print(f"Encode right {x}")
    x = down(x)
    #print(f"Encode down {x}")
    x = left(x,d)
    #print(f"Encode left {x}")
    return x

def decode(cipher):
    d=24
    x = cipher
    x = left_d(x,d)
    #print(f"Decode left {x}")
    x = down_d(x)
    #print(f"Decode down {x}")
    x = right_d(x,d)
    x = up_d(x)
    return x


def main():
    flag = "redacted"
    encoded = encode(flag)
    
    print("What does this mean?")
    encoded = "1010000011111000101010101000001010100100110110001111111010001000100000101000111011000100101111011001100011011000101011001100100010011001110110001001000010001100101111001110010011001100"
    print(encoded)

    print(decode(encoded))

if __name__ == "__main__":
  main()

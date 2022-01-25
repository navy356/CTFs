def xor(b1, b2):
    xored = ''
    for i, j in zip(b1, b2):
        xor_one = '{:02x}'.format(i ^ j)
        xored += xor_one
    return bytearray.fromhex(xored)

homework = open('message.wav','rb').read()

# source: https://asecuritysite.com/forensics/magic
magic_number = 280687
key = (magic_number * (len(homework)//4 + 1))[:len(homework)]
output = xor(homework, key)

f = open('message', 'wb')
f.write(output)
f.close()

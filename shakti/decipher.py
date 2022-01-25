from pwn import xor
f = open('cipher.jpg', 'rb')
c = f.read()
f.close()

key = xor(bytes.fromhex('ffd8ffe000104a4649460001'), bytes.fromhex('88e892d36e4f3a763e757276'))

p = xor(c, key[:11])
with open('decrypt.jpg', 'wb') as f:
    f.write(p)

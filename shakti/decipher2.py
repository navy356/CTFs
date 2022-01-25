from pwn import xor
f = open("cipher.txt", "rb")
a = f.read()
f.close()
print(f"Length of the string: {len(a)}")
m1 = b'Message : shaktictf{' + b'X'
l1 = len(m1)
key1 = xor(m1, a[:l1])

m2 = b'}:e.o.m'
l2 = len(m2)
key2 = xor(m2, a[-l2:])

key = key1+key2
print(xor(key, a))

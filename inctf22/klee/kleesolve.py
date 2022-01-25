import sys
from pwn import *

def pad(str,l):
    if len(str)>l:
        return str[0:l]
    while(len(str)<l):
        str=str+'A'
    return str
if __name__ == "__main__":
    io = process('./klee.py')
    io.sendlineafter("Key: ","13370131377037711071301337073137371073717307137")
    io.sendlineafter("Flag: ",pad("inctf{",0x2f))
    io.recvuntil("Encrypted string\n")
    print(io.recvline())
    io.close()

    io = process('./run.sh')
    io.sendlineafter("Key: ","13370131377037711071301337073137371073717307137")
    io.sendlineafter("Flag: ",pad("inctf{",0x2f))
    io.recvuntil("Encrypted string\n")
    print(io.recvline())
    io.close()
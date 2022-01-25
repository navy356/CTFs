section	.text
   global _start   

_start:
    lea rsi,x
    mov rdi,3
foofoo:
    mov rbx,rsi
    mov rcx,0
    mov rdx,rdi
    sub rdx,2
    add rbx,8
argloop:
    add rbx,8
    xor rax,rax
    mov r10,[rbx]
    mov al,[r10]
    sub rax,'0'
    push rax
    push rcx
    push rdx
    mov rcx,3
    call modulo
    pop rdx
    pop rcx
    cmp rax,0
    je zero
notzero:
    pop rax
    add rcx,rax
    add rcx,rax
    jmp endif
zero:
    pop rax
    add rcx,rax

endif:
    dec rdx
    cmp rdx,0
    je exit
    jmp argloop

modulo:
    cqo
    idiv rcx
    mov rax,rdx
    ret

exit:
    mov rax,rcx
    mov rcx,100
    call modulo
    mov rdi,rax
    mov rax,0x3c
    syscall
    jmp exit

section	.data
    global x
    x:    
    db  '2'
    db  '4'
    db  '3'
    db  '5'
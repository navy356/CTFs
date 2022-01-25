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
    push rcx
    push rdx
    call atoi
    pop rdx
    pop rcx
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

atoi:
    xor rax,rax
    mov al,[r10]
    cmp rax,0
    sub rax,'0'
    mov r11,10
    mul r11
    mov rcx,rax
    xor rax,rax
    mov eax,[r10]
    shr rax,8
    and rax,0xff
    cmp rax,0
    je skip
    sub rax,'0'
skip:
    add rcx,rax
    mov rax,rcx
    ret
 
exit:
    mov rax,rcx
    mov rcx,100
    call modulo
    mov rdi,rax
    mov rax,0x3c
    syscall
    jmp exit
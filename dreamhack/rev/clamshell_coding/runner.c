#include <unistd.h>
#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <seccomp.h>
#include <sys/mman.h>

// from https://gist.github.com/clzola/b7f1d9dfacab7a253afce7ca3d5aeb23
unsigned char* hex2bin(const char *hexstr, size_t *size)
{
    size_t hexstrLen = strlen(hexstr);
    size_t bytesLen = hexstrLen / 2;

    unsigned char *bytes = (unsigned char*) malloc(bytesLen);

    unsigned long page_start = ((unsigned long) bytes) & 0xfffffffffffff000;

    int page_size;
    page_size = sysconf(_SC_PAGE_SIZE);    
    if (page_size == -1)
       perror("[-] sysconf failed");

    int ret = mprotect((void *) page_start, 0x1000, PROT_READ | PROT_WRITE | PROT_EXEC);
    if(ret<0){ 
        //perror("[-] mprotect failed"); 
    }

    int count = 0;
    const char *pos = hexstr;

    for(count = 0; count < bytesLen; count++) {
        sscanf(pos, "%2hhx", &bytes[count]);
        pos += 2;
    }

    if( size != NULL )
        *size = bytesLen;

    return bytes;
}


int main(int argc, char* argv[]){
    assert(argc > 1);
    assert(strlen(argv[1]) % 2 == 0);

    /*for(int i=0; i<argc; i++)
    {
        puts(argv[i]);
    }*/
    size_t len = 0;
    char *shellcode = hex2bin(argv[1], &len);
    assert(strlen(argv[1]) == len * 2);

    /*scmp_filter_ctx ctx = seccomp_init(SCMP_ACT_KILL);
    seccomp_rule_add(ctx, SCMP_ACT_ALLOW, SCMP_SYS(exit), 0);
    seccomp_rule_add(ctx, SCMP_ACT_ALLOW, SCMP_SYS(exit_group), 0);
    seccomp_load(ctx);*/

    int (*foo)(int, char*[]) = (int(*)(int, char*[]))shellcode;

    return foo(argc, argv);
}

#define FFI_LIB "./crypt.so"
#define FFI_SCOPE "crypt"

void encrypt(void* in,unsigned int size,unsigned long long key,void* out);
void decrypt(void* in,unsigned int size,unsigned long long key,void* out);

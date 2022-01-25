from os import urandom
JWT_SECRET = urandom(32)
try:
    FLAG = open('flag.txt', 'r').read()
except:
    FLAG = 'DH{zzzzzmdfklamsdklfmasdklfmasdklfl}'

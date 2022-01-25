from itertools import cycle
from time import time
from hashlib import md5
from secret import flag
import os

def xor(a,b):
    return ''.join(chr(ord(i)^ord(j)) for i,j in zip(a,cycle(b)))


def hashit(x) :
    return md5(x).hexdigest()

def generatekey(sz) :
    return os.urandom(sz)


def encrypt(m) :
    return xor("Message : " + m + ":e.o.m" ,generatekey(28))

if __name__ == "__main__" :
    f = open("cipher.txt","w")
    f.write(encrypt(flag))
    f.close()



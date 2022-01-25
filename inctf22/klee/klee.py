#!/usr/bin/env python
import sys
def encrypt(key,flag):
    i=0
    flag=list(flag)
    keyLen=0
    while True:
        keyLen = len(key)
        if i>=keyLen:
            break
        key_i = ord(key[i])
        if key_i == 0x37:
            flag[i]=chr(((ord(flag[i])^0x13)-0x31)^9)
        elif key_i > 0x37:
            print('error')
        elif key_i == 0x33:
            flag[i]=chr((ord(flag[i])^0x13)-0x31)
        else:
            if key_i > 0x33:
                print('error')
            if key_i == 0x30:
                flag[i]=chr(ord(flag[i])^0x13)
            else:
                if key_i!=0x31:
                    print('error')
                flag[i]=chr((ord(flag[i])^0x13)-0x37)
        i+=1  

    return flag

def decrypt(key,flag):
    i=0
    flag_new=['0']*len(flag)
    keyLen=0
    while True:
        keyLen = len(key)
        if i>=keyLen:
            break
        key_i = ord(key[i])
        if key_i == 0x37:
            #flag[i]=chr(((ord(flag[i])^0x13)-0x31)^9)
            flag_new[i]=chr(((flag[i]^9)+0x31)^0x13)
        elif key_i > 0x37:
            print('error')
        elif key_i == 0x33:
            #flag[i]=chr((ord(flag[i])^0x13)-0x31)
            flag_new[i]=chr((flag[i]+0x31)^0x13)
        else:
            if key_i > 0x33:
                print('error')
            if key_i == 0x30:
                #flag[i]=chr(ord(flag[i])^0x13)
                flag_new[i]=chr(flag[i]^0x13)
            else:
                if key_i!=0x31:
                    print('error')
                #flag[i]=chr((ord(flag[i])^0x13)-0x37)
                flag_new[i]=chr((flag[i]+0x37)^0x13)
        i+=1  


    return ''.join(flag_new)

def print_encrypted_flag(flag):
    for ch in flag:
        print(ord(ch),end=" ")

    print()

def main():
    key = input("Key: ")
    flag  = input("Flag: ")

    key_len = len(key)
    flag_len = len(flag)
    key_l2=0

    if(key_len!=flag_len):
        key_l2=len(key)
        if(key_l2!=0x2f):
            print('Invalid input')
    if(key_len==flag_len or (key_len!=flag_len and key_l2==0x2f)):
        if(len(flag)==0x2f):
            flag=encrypt(key,flag)
            print("Decrypted string")
            code=[67,76,63,63,117,66,55,71,53,71,63,34,27,-26,69,57,42,106,59,48,27,119,-23,63,48,48,99,63,27,62,53,69,63,63,-21,35,69,47,18,-16,57,-17,76,77,47,76,52]
            print(decrypt(key,code))
            print("Encrypted string")
            print_encrypted_flag(flag)
        else:
            print("Invalid input")

main()
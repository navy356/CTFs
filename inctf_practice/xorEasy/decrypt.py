import string

def show(flag):
    for i in flag:
            if i is None:
                print('?',end="")
            else:
                print(i,end="")

def crack(known,endKnown):
    with open('cipher.txt','rb') as f:
        text=f.readline()
        crib=list(known)
        l=len(known)
        key=[None]*28
        flag=[None]*56

        for i in range(0,l):
            key[i]=ord(crib[i])^text[i]
            flag[i]=key[i]

        #key[l]=ord('')^text[28+l]
        for i in range(0,len(endKnown),1):
            key[len(key)-1-i]=ord(endKnown[len(endKnown)-1-i])^text[len(text)-1-i]
        for i in range(0,28):
            if key[i] is not None:
                flag[i]=chr(text[i]^key[i])
                flag[i+28]=chr(text[i+28]^key[i])

        for i in range(0,255):
            flag[l]=chr(i^text[l])
            flag[l+28]=chr(i^text[l+28])
            show(flag)
            print()
                    
        for i in flag:
            if i is None:
                print('?',end="")
            else:
                print(i,end="")

        print()

crack("Message : shaktictf{","}:e.o.m")
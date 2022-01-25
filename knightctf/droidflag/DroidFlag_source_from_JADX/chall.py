import requests

TARGET_URL = 'http://159.223.166.39:9004'

def make_res(eq):
    data = {
        "equation":eq
    }

    res = requests.post(f"{TARGET_URL}/",data=data)
    print(res.text)

#make_res('1'*79)
make_res('\na')
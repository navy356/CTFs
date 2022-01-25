import requests
import signal
import sys
import os

TARGET_URL = 'http://127.0.0.1:1024'

def signal_handler(sig, frame):
    print('\nExiting...')
    sys.exit(0)

def register(username,password):
    headers = {'Content-type': 'application/json'}
    data = {
        "username" : username,
        "password" : password
    }

    res=requests.post(TARGET_URL+'/register',json=data,headers=headers)
    print(res.text)
    input("Press enter to continue")

def login(username,password):
    headers = {'Content-type': 'application/json'}
    data = {
        "username" : username,
        "password" : password
    }

    res=requests.post(TARGET_URL+'/login',json=data,headers=headers)
    print(res.text)
    input("Press enter to continue")

def menu():
    while True:
        os.system('clear')
        signal.signal(signal.SIGINT, signal_handler)
        print("1) Register")
        print("2) Login")
        print("Press Ctrl+C to exit")

        option=int(input("Enter an option: "))
        
        if(option==1):
            username=input("Username: ")
            password=input("Password: ")
            register(username,password)

        elif(option==2):
            username=input("Username: ")
            password=input("Password: ")
            login(username,password)

        else:
            pass


menu()
import requests
import signal
import sys
import os
from urllib.parse import quote_plus, unquote_plus

TARGET_URL='http://localhost'

def signal_handler(sig, frame):
    print('Exiting...')
    sys.exit(0)

def header(payload):
    r=requests.get(TARGET_URL+'/find?debug=t&'+payload)
    return(r.headers)

def main():
    while True:
        os.system('clear')
        signal.signal(signal.SIGINT, signal_handler)
        q=input("Header Query: ")
        h=header(q)
        for k,v in h.items():
            t=quote_plus(v)
            print("{}:{}:{}".format(k,v,t))
        cont=input("Press enter to continue")

def test():
    with open('pay.txt','r') as f:
        lines = f.read().splitlines()
    for l in lines:
        q='A=b'+l
        print(q)
        h=header(q)
        for k,v in h.items():
            if k=='A':
                print("{}:{}".format(k,v))
            elif k=='Header-Test':
                print("Success")
                print("{}:{}".format(k,v))
                exit(0)

main()
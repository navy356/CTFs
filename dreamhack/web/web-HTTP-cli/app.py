#!/usr/bin/python3
import urllib.request
import socket

try:
    FLAG = open('./flag.txt', 'r').read()
except:
    FLAG = '[**FLAG**]'

def get_host_port(url):
    return url.split('://')[1].split('/')[0].lower().split(':')


with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind(('', 8001))
    s.listen()

    while True:
        try:
            cs, ca = s.accept()
            cs.sendall('[Input Example]\n'.encode())
            cs.sendall('> https://dreamhack.io:443/\n'.encode())
        except:
            continue
        while True:
            cs.sendall('> '.encode())
            url = cs.recv(1024).decode().strip()
            print(url)
            if len(url) == 0:
                break
            try:
                (host, port) = get_host_port(url)
                print(host,port)
                if 'localhost' == host:
                    cs.sendall('cant use localhost\n'.encode())
                    continue
                if 'dreamhack.io' != host:
                    if '.' in host:
                        cs.sendall('cant use .\n'.encode())
                        continue
                print(url)
                print('-----------------')
                #print(urllib.request.urlopen(url).read())
                #print(urllib.request.urlopen('file:///etc/passwd').read())
                print('-----------------')
                cs.sendall('result: '.encode() + urllib.request.urlopen(url).read())
            except:
                cs.sendall('error\n'.encode())
        cs.close()
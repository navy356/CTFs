import requests
import urllib3
rotimi = open("shattered-1.pdf", 'rb+').read()[:500]
letmein = open("shattered-2.pdf", 'rb+').read()[:500]
 
r = requests.post('https://chall.ctf.pragyan.org:30777/', data={'provebot1': rotimi, 'provebot2': letmein}, headers={"User-Agent": "bot"})
print(r.text)
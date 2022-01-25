import string
import hashlib
import sys 
import random

while True:
    a=''.join(random.choices(string.ascii_uppercase + string.digits, k=4))
    if hashlib.sha256(a.encode('utf-8')).hexdigest()[:4] == sys.argv[1]:
        print(a)
        break
import os

len = 'a'
while True:
    check=os.popen(f'echo "inctf{{{len}}}"|./chall 912985153').read()
    len+='a'
    print(f'inctf{{{len}}}')
    if(check.strip()!='Input: Incorrect length'):
        print(check)
        break
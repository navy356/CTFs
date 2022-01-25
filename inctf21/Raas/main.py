import requests
import re
import io
import socket
from urllib.parse import urlparse
from urllib.parse import unquote_plus
import os
from modules.Gophers import GopherAdapter
from modules.files import LocalFileAdapter

def Requests_On_Steroids(url):
    try:
        s = requests.Session()
        s.mount("inctf:",GopherAdapter())
        s.mount('file://',LocalFileAdapter())
        resp = s.get(url)
        assert resp.status_code == 200
        return(resp.text)
    except Exception as e: 
        print(e)
        return "SOME ISSUE OCCURED" 
        # resp = s.get("butts://127.0.0.1:6379/_get dees")

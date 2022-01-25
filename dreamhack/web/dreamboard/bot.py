#!/usr/bin/env python3.8
from selenium import webdriver
from requests import get, post
from config import *
from time import sleep

options = webdriver.ChromeOptions()
options.add_argument('--headless')
options.add_argument('--no-sandbox')
options.add_argument('--disable-dev-shm-usage')

driver = webdriver.Chrome('/chromedriver', options=options)
driver.implicitly_wait(3)
driver.set_page_load_timeout(3)
driver.get('http://127.0.0.1:80/')

HOST = 'http://127.0.0.1:8000'

def get_token(username, password):
    url = f'{HOST}/user/token'
    c = post(url, json={'username': username, 'password': password})
    return c

def report_list(token):
    url = f'{HOST}/admin/report/list'
    c = get(url, headers={'Authorization': f'JWT {token}'})
    return c

def admin_report_set(token, report_id, msg):
    url = f'{HOST}/admin/report/set'
    c = post(url, headers={'Authorization': f'JWT {token}'}, json={'id': report_id, 'msg': msg})
    return c

c = get_token(ADMIN_USERNAME, ADMIN_PASSWORD)
token = c.json()['msg']['token']

# {"error": false, "msg": [{"id": 3, "post_id": "3", "msg": ""}, {"id": 4, "post_id": "7", "msg": ""}]}

while True:
    c = report_list(token)
    reports = c.json()['msg']
    if len(reports) == 0:
        print('no reports found, sleep for 5 seconds')
        sleep(5)

    for report in reports:
        post_id = report['post_id']
        report_id = report['id']
        print(f'check report.. {report_id} => {post_id}')
        try:
            driver.execute_script(f"localStorage.setItem('token', '{token}');")
            driver.get(f'http://127.0.0.1:80/board/{post_id}')
            admin_report_set(token, report_id, 'checked')
        except:
            driver.get(f'http://127.0.0.1:80/')
            admin_report_set(token, report_id, 'error occured')

        sleep(1)

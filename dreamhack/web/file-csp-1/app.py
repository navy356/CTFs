#!/usr/bin/env python3
import os
import shutil
from time import sleep
from urllib.parse import quote

from flask import Flask, request, render_template, redirect, make_response
from selenium import webdriver

from flag import FLAG

APP = Flask(__name__)


@APP.route('/')
def index():
    return render_template('index.html')


@APP.route('/test', methods=['GET', 'POST'])
def test_csp():
    global CSP
    if request.method == 'POST':
        csp = request.form.get('csp')
        # start bot..
        try:
            options = webdriver.ChromeOptions()
            for _ in ['headless', 'window-size=1920x1080', 'disable-gpu', 'no-sandbox', 'disable-dev-shm-usage']:
                options.add_argument(_)
            driver = webdriver.Chrome('/chromedriver', options=options)
            driver.implicitly_wait(3)
            driver.set_page_load_timeout(3)
            driver.get(f'http://localhost:8000/live?csp={quote(csp)}')
            try:
                a = driver.execute_script('return a()');
            except:
                a = 'error'
            try:
                b = driver.execute_script('return b()');
            except:
                b = 'error'
            try:
                c = driver.execute_script('return c()');
            except Exception as e:
                c = 'error'
                c = e
            try:
                d = driver.execute_script('return $(document)');
            except:
                d = 'error'

            if a == 'error' and b == 'error' and c == 'c' and d != 'error':
                return FLAG

            return f'Try again!, {a}, {b}, {c}, {d}'
        except Exception as e:
            return f'An error occured!, {e}'

    return render_template('check.html')


@APP.route('/live', methods=['GET'])
def live_csp():
    csp = request.args.get('csp', '')
    resp = make_response(render_template('csp.html'))
    resp.headers.set('Content-Security-Policy', csp)
    return resp


if __name__ == '__main__':
    APP.run(host='0.0.0.0', port=8000, debug=True, threaded=True)

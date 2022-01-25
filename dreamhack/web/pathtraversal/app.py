#!/usr/bin/python3
from flask import Flask, request, render_template, abort
from functools import wraps
import requests
import os, json

users = {
    '0': {
        'userid': 'guest',
        'level': 1,
        'password': 'guest'
    },
    '1': {
        'userid': 'admin',
        'level': 9999,
        'password': 'admin'
    }
}

def internal_api(func):
    @wraps(func)
    def decorated_view(*args, **kwargs):
        if request.remote_addr == '127.0.0.1':
            return func(*args, **kwargs)
        else:
            abort(401)
    return decorated_view

app = Flask(__name__)
app.secret_key = os.urandom(32)
API_HOST = 'http://127.0.0.1:8000'

try:
    FLAG = open('./flag.txt', 'r').read() # Flag is here!!
except:
    FLAG = '[**FLAG**]'

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/get_info', methods=['GET', 'POST'])
def get_info():
    if request.method == 'GET':
        return render_template('get_info.html')
    elif request.method == 'POST':
        userid = request.form.get('userid', '')
        info = requests.get(f'{API_HOST}/api/user/{userid}').text
        return render_template('get_info.html', info=info)

@app.route('/api')
@internal_api
def api():
    return '/user/<uid>, /flag'

@app.route('/api/user/<uid>')
@internal_api
def get_flag(uid):
    try:
        info = users[uid]
    except:
        info = {}
    return json.dumps(info)

@app.route('/api/flag')
@internal_api
def flag():
    return FLAG

application = app # app.run(host='0.0.0.0', port=8000)
# Dockerfile
#     ENTRYPOINT ["uwsgi", "--socket", "0.0.0.0:8000", "--protocol=http", "--threads", "4", "--wsgi-file", "app.py"]
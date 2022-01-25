#!/usr/bin/env python3
from flask import Flask, request, render_template, redirect
import os, pickle, base64

app = Flask(__name__)
app.secret_key = os.urandom(32)

try:
    FLAG = open('./flag.txt', 'r').read() # Flag is here!!
except:
    FLAG = '[**FLAG**]'

INFO = ['name', 'userid', 'password']

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/create_session', methods=['GET', 'POST'])
def create_session():
    if request.method == 'GET':
        return render_template('create_session.html')
    elif request.method == 'POST':
        info = {}
        for _ in INFO:
            info[_] = request.form.get(_, '')
        data = base64.b64encode(pickle.dumps(info)).decode('utf8')
        return render_template('create_session.html', data=data)

@app.route('/check_session', methods=['GET', 'POST'])
def check_session():
    if request.method == 'GET':
        return render_template('check_session.html')
    elif request.method == 'POST':
        session = request.form.get('session', '')
        info = pickle.loads(base64.b64decode(session))
        return render_template('check_session.html', info=info)

app.run(host='0.0.0.0', port=8000)
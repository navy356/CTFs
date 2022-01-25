#!/usr/bin/python3
from flask import Flask, request, render_template, make_response, redirect, url_for
import socket

app = Flask(__name__)

try:
    FLAG = open('./flag.txt', 'r').read()
except:
    FLAG = '[**FLAG**]'

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/socket', methods=['GET', 'POST'])
def login():
    if request.method == 'GET':
        return render_template('socket.html')
    elif request.method == 'POST':
        host = request.form.get('host')
        port = request.form.get('port', type=int)
        data = request.form.get('data')

        retData = ""
        try:
            with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
                s.settimeout(3)
                s.connect((host, port))
                s.sendall(data.encode())
                while True:
                    tmpData = s.recv(1024)
                    retData += tmpData.decode()
                    if not tmpData: break
            
        except Exception as e:
            return render_template('socket_result.html', data=e)
        
        return render_template('socket_result.html', data=retData)


@app.route('/admin', methods=['POST'])
def admin():
    if request.remote_addr != '127.0.0.1':
        return 'Only localhost'

    if request.headers.get('User-Agent') != 'Admin Browser':
        return 'Only Admin Browser'

    if request.headers.get('DreamhackUser') != 'admin':
        return 'Only Admin'

    if request.cookies.get('admin') != 'true':
        return 'Admin Cookie'

    if request.form.get('userid') != 'admin':
        return 'Admin id'

    return FLAG

app.run(host='0.0.0.0', port=8000)
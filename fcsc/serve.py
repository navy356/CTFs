# /usr/bin/env python3
# -*- coding:utf-8 -*-
# -*- requirements:requirements.txt -*-

# Congrats! Here is the flag for Baguette VPN 1/2
#   FCSC{e5e3234f8dae908461c6ee777ee329a2c5ab3b1a8b277ff2ae288743bbc6d880}

import os
import urllib3
import sys
from flask import Flask, request, jsonify, Response
app = Flask(__name__)


pool_manager = urllib3.PoolManager()

@app.route('/')
def index():
    with open('index.html', 'r') as myfile:
        return myfile.read()


@app.route('/api')
def api():
    return Response('OK', status=200)


@app.route("/api/image")
def image():
    filename = request.args.get("fn")
    if filename:
        http = urllib3.PoolManager()
        info = pool_manager.request('GET', 'http://localhost:5000'+filename).info()
        print(info)
        return http.request('GET', 'http://localhost:5000' + filename).data
    else:
        return Response('Paramètre manquant', status=400)


@app.route("/api/secret")
def admin():
    if request.remote_addr == '127.0.0.1':
        if request.headers.get('X-API-KEY') == 'b99cc420eb25205168e83190bae48a12':
            return jsonify({"secret": os.getenv('FLAG')})
        return Response('Interdit: mauvaise clé d\'API', status=403)
    return Response('Interdit: mauvaise adresse IP', status=403)


@app.route("/api/debug")
def debug():
    data = {}
    for k, v in globals().copy().items():
        if not isinstance(v, str):
            data[k] = str(dir(v))
        else:
            data[k] = v
    data['__version__'] = sys.version
    return jsonify(data)


@app.route('/<path:path>')
def load_page(path):
    if '..' in path:
        return Response('Interdit', status=403)
    try:
        with open(path, 'r') as myfile:
            mime = 'text/' + path.split('.')[-1]
            return Response(myfile.read(), mimetype=mime)
    except Exception as e:
        return Response(str(e), status=404)


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=os.getenv('FLASK_LOCAL_PORT'))
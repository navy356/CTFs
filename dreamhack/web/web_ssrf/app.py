#!/usr/bin/python3
from flask import (
    Flask,
    request,
    render_template
)
import http.server
import threading
import requests
import os, random, base64
from urllib.parse import urlparse

app = Flask(__name__)
app.secret_key = os.urandom(32)

try:
    FLAG = open("./flag.txt", "r").read()  # Flag is here!!
except:
    FLAG = "[**FLAG**]"


@app.route("/")
def index():
    return render_template("index.html")


@app.route("/img_viewer", methods=["GET", "POST"])
def img_viewer():
    if request.method == "GET":
        return render_template("img_viewer.html")
    elif request.method == "POST":
        url = request.form.get("url", "")
        urlp = urlparse(url)
        if url[0] == "/":
            url = "http://localhost:8000" + url
        elif ("localhost" in urlp.netloc) or ("127.0.0.1" in urlp.netloc):
            data = open("error.png", "rb").read()
            img = base64.b64encode(data).decode("utf8")
            return render_template("img_viewer.html", img=img)
        try:
            data = requests.get(url, timeout=3).content
            img = base64.b64encode(data).decode("utf8")
        except:
            data = open("error.png", "rb").read()
            img = base64.b64encode(data).decode("utf8")
        return render_template("img_viewer.html", img=img)


local_host = "127.0.0.1"
local_port = random.randint(1500, 1800)
local_server = http.server.HTTPServer(
    (local_host, local_port), http.server.SimpleHTTPRequestHandler
)


def run_local_server():
    local_server.serve_forever()


threading._start_new_thread(run_local_server, ())

app.run(host="0.0.0.0", port=8000, threaded=True)

#!/usr/bin/env python
from flask import Flask, redirect, url_for, request
from jinja2 import Template, Environment, PackageLoader
#config['FLAG']='testflag'
app = Flask(__name__)
@app.route('/')
# ‘/’ URL is bound with hello_world() function.
def hello_world():
    name = request.args.get('name')
    
    tm = Template(f"Hello {name}")
    msg = tm.render(name=name)
    return msg

if __name__ == '__main__':
    print(app.config)
    app.run(debug = True)
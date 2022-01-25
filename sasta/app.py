from flask import Flask, jsonify, session, request
from flask_cookie_decode import CookieDecode

app = Flask(__name__)
app.config.update({'SECRET_KEY': ''})
cookie = CookieDecode()
cookie.init_app(app)

@app.route('/')
def index():
    a = request.args.get('a')
    session['a'] = a
    return jsonify(dict(session))
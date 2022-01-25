#!/usr/bin/python3
from flask import Flask, request, render_template_string, g, session, jsonify
import sqlite3
import os, hashlib

app = Flask(__name__)
app.secret_key = "Th1s_1s_V3ry_secret_key"

def get_db():
  db = getattr(g, '_database', None)
  if db is None:
    db = g._database = sqlite3.connect(os.environ['DATABASE'])
  db.row_factory = sqlite3.Row
  return db

def query_db(query, args=(), one=False):
  cur = get_db().execute(query, args)
  rv = cur.fetchall()
  cur.close()
  return (rv[0] if rv else None) if one else rv

@app.teardown_appcontext
def close_connection(exception):
  db = getattr(g, '_database', None)
  if db is not None:
    db.close()

@app.route('/')
def index():
  return "api-server"

@app.route('/api/me')
def me():
  if session.get('uid'):
    return jsonify(userid=session['uid'])
  return jsonify(userid=None)

@app.route('/api/login', methods=['POST'])
def login():
  userid = request.form.get('userid', '')
  password = request.form.get('password', '')
  if userid and password:
    ret = query_db(f"SELECT * FROM users where userid='{userid}' and password='{hashlib.sha256(password.encode()).hexdigest()}'" , one=True)
    if ret:
      session['uid'] = ret[0]
      return jsonify(result="success", userid=ret[0])
  return jsonify(result="fail")

@app.route('/api/logout')
def logout():
  session.pop('uid', None)
  return jsonify(result="success")

@app.route('/api/join', methods=['POST'])
def join():
  userid = request.form.get('userid', '')
  password = request.form.get('password', '')
  if userid and password:
    conn = get_db()
    cur = conn.cursor()
    cur.execute("Insert into users values(?, ?);", (userid, hashlib.sha256(password.encode()).hexdigest()))
    conn.commit()
    return jsonify(result="success")
  return jsonify(result="error")

@app.route('/api/memo/add', methods=['PUT'])
def memoAdd():
  if not session.get('uid'):
    return jsonify(result="no login")

  userid = session.get('uid')
  title = request.form.get('title')
  contents = request.form.get('contents')

  if title and contents:
    conn = get_db()
    cur = conn.cursor()
    ret = cur.execute("Insert into memo(userid, title, contents) values(?, ?, ?);", (userid, title, contents))
    conn.commit()
    return jsonify(result="success", memoidx=ret.lastrowid)
  return jsonify(result="error")

@app.route('/api/memo/<idx>', methods=['GET'])
def memoView(idx):
  mode = request.args.get('mode', 'json')
  ret = query_db("SELECT * FROM memo where idx=" + idx)[0]
  if ret:
    userid = ret['userid']
    title = ret['title']
    contents = ret['contents']
    if mode == 'html':
      template = ''' Written by {userid}<h3>{title}</h3>
      <pre>{contents}</pre>
      '''.format(title=title, userid=userid, contents=contents)
      return render_template_string(template)
    else:
      return jsonify(result="success",
        userid=userid,
        title=title,
        contents=contents)
  return jsonify(result="error")

@app.route('/api/memo/<int:idx>', methods=['PUT'])
def memoUpdate(idx):
  if not session.get('uid'):
    return jsonify(result="no login")

  ret = query_db('SELECT * FROM memo where idx=?', [idx,])[0]
  userid = session.get('uid')
  title = request.form.get('title')
  contents = request.form.get('contents')

  if ret and title and contents:
    conn = get_db()
    cur = conn.cursor()
    updateRet = cur.execute("UPDATE memo SET title=?, contents=? WHERE idx=?",(title, contents, idx))
    conn.commit()
    if updateRet:
      return jsonify(result="success")
  return jsonify(result="error")
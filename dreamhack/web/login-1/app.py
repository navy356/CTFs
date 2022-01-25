#!/usr/bin/python3
from flask import Flask, request, render_template, make_response, redirect, url_for, session, g
import sqlite3
import hashlib
import os
import time, random

app = Flask(__name__)
app.secret_key = os.urandom(32)

DATABASE = "database.db"

userLevel = {
    0 : 'guest',
    1 : 'admin'
}
MAXRESETCOUNT = 5

try:
    FLAG = open('./flag.txt', 'r').read()
except:
    FLAG = '[**FLAG**]'

def makeBackupcode():
    return random.randrange(100)

def get_db():
    db = getattr(g, '_database', None)
    if db is None:
        db = g._database = sqlite3.connect(DATABASE)
    db.row_factory = sqlite3.Row
    return db

@app.teardown_appcontext
def close_connection(exception):
    db = getattr(g, '_database', None)
    if db is not None:
        db.close()

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/login', methods=['GET', 'POST'])
def login():
    if request.method == 'GET':
        return render_template('login.html')
    else:
        userid = request.form.get("userid")
        password = request.form.get("password")

        conn = get_db()
        cur = conn.cursor()
        user = cur.execute('SELECT * FROM user WHERE id = ? and pw = ?', (userid, hashlib.sha256(password.encode()).hexdigest() )).fetchone()
        
        if user:
            session['idx'] = user['idx']
            session['userid'] = user['id']
            session['name'] = user['name']
            session['level'] = userLevel[user['level']]
            return redirect(url_for('index'))

        return "<script>alert('Wrong id/pw');history.back(-1);</script>";

@app.route('/logout')
def logout():
    session.clear()
    return redirect(url_for('index'))

@app.route('/register', methods=['GET', 'POST'])
def register():
    if request.method == 'GET':
        return render_template('register.html')
    else:
        userid = request.form.get("userid")
        password = request.form.get("password")
        name = request.form.get("name")

        conn = get_db()
        cur = conn.cursor()
        user = cur.execute('SELECT * FROM user WHERE id = ?', (userid,)).fetchone()
        if user:
            return "<script>alert('Already Exists userid.');history.back(-1);</script>";

        backupCode = makeBackupcode()
        sql = "INSERT INTO user(id, pw, name, level, backupCode) VALUES (?, ?, ?, ?, ?)"
        cur.execute(sql, (userid, hashlib.sha256(password.encode()).hexdigest(), name, 0, backupCode))
        conn.commit()
        return render_template("index.html", msg=f"<b>Register Success.</b><br/>Your BackupCode : {backupCode}")

@app.route('/forgot_password', methods=['GET', 'POST'])
def forgot_password():
    if request.method == 'GET':
        return render_template('forgot.html')
    else:
        userid = request.form.get("userid")
        newpassword = request.form.get("newpassword")
        backupCode = request.form.get("backupCode", type=int)

        conn = get_db()
        cur = conn.cursor()
        user = cur.execute('SELECT * FROM user WHERE id = ?', (userid,)).fetchone()
        if user:
            # security for brute force Attack.
            time.sleep(1)

            if user['resetCount'] == MAXRESETCOUNT:
                return "<script>alert('reset Count Exceed.');history.back(-1);</script>"
            
            if user['backupCode'] == backupCode:
                newbackupCode = makeBackupcode()
                updateSQL = "UPDATE user set pw = ?, backupCode = ?, resetCount = 0 where idx = ?"
                cur.execute(updateSQL, (hashlib.sha256(newpassword.encode()).hexdigest(), newbackupCode, str(user['idx'])))
                msg = f"<b>Password Change Success.</b><br/>New BackupCode : {newbackupCode}"

            else:
                updateSQL = "UPDATE user set resetCount = resetCount+1 where idx = ?"
                cur.execute(updateSQL, (str(user['idx'])))
                msg = f"Wrong BackupCode !<br/><b>Left Count : </b> {(MAXRESETCOUNT-1)-user['resetCount']}"
            
            conn.commit()
            return render_template("index.html", msg=msg)

        return "<script>alert('User Not Found.');history.back(-1);</script>";


@app.route('/user/<int:useridx>')
def users(useridx):
    conn = get_db()
    cur = conn.cursor()
    user = cur.execute('SELECT * FROM user WHERE idx = ?;', [str(useridx)]).fetchone()
    
    if user:
        return render_template('user.html', user=user)

    return "<script>alert('User Not Found.');history.back(-1);</script>";

@app.route('/admin')
def admin():
    if session and (session['level'] == userLevel[1]):
        return FLAG

    return "Only Admin !"

app.run(host='0.0.0.0', port=8000)

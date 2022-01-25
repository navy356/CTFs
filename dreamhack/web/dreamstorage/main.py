#!/usr/bin/python3
from flask import Flask, request, render_template, make_response, redirect, url_for, g, flash
from werkzeug.security import generate_password_hash, check_password_hash
import os, binascii
from flask_jwt_extended import JWTManager, jwt_required, create_access_token, current_user
from sqlalchemy.exc import IntegrityError, DataError
from models import Users, Storages, init_db
from selenium import webdriver
from promise import Promise

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///dreamstorage.db'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.secret_key = os.urandom(32)
db = init_db(app)

app.config["JWT_TOKEN_LOCATION"] = ["cookies"]
app.config["JWT_COOKIE_SECURE"] = True
app.config["JWT_ACCESS_COOKIE_NAME"] = 'dream-token'
app.config["JWT_COOKIE_CSRF_PROTECT"] = False
jwt = JWTManager(app)

ADMIN_USERNAME = 'administrator'
ADMIN_PASSWORD =  binascii.hexlify(os.urandom(32))
try:
    FLAG = open('./flag.txt', 'r').read()
except:
    FLAG = '[**FLAG**]'

@jwt.user_lookup_loader
def _user_lookup_callback(_jwt_header, jwt_data):
    identity= jwt_data["sub"]
    user = Users.query.filter_by(id=identity).first()
    if user is None:
        return None
    return user

@app.before_first_request
def insert_initial_data():
    try:
        admin = Users(
            uid=str(ADMIN_USERNAME),
            upw=generate_password_hash(str(ADMIN_PASSWORD)),
            is_admin=True
        )
        db.session.add(admin)
        db.session.commit()
    except IntegrityError:
        db.session.rollback()
        return

@app.after_request
def after_request(response):
    response.headers["Content-Security-Policy"] = "default-src 'self' 'unsafe-inline' https://cdn.jsdelivr.net"
    response.headers["X-Frame-Options"] = "deny"
    return response

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/signin', methods=['GET', 'POST'])
def signin():
    if request.method == 'POST':
        uid = request.form.get('uid')
        upw = request.form.get('upw')

        if uid and upw:
            user = Users.query.filter_by(uid=uid).first()
            if user and check_password_hash(user.upw, upw):
                access_token = create_access_token(identity=user.id, expires_delta=None)
                response = make_response(redirect(url_for('user')))
                response.set_cookie(app.config["JWT_ACCESS_COOKIE_NAME"], access_token, path='/user')
                if user.is_admin:
                    response.set_cookie('FLAG', FLAG, path='/user')
                return response
        
        flash('signin fail.')
        return redirect(url_for('signin'))

    elif request.method == 'GET':
        return render_template('signin.html')

@app.route('/signup', methods=['GET', 'POST'])
def signup():
    if request.method == 'POST':
        uid = request.form.get('uid')
        upw = request.form.get('upw')

        if uid and upw:
            if len(uid) < 5 or len(upw) < 5:
                flash('uid.length > 5 and upw.length > 5')
                return redirect(url_for('signup'))
            user = Users.query.filter_by(uid=uid).first()
            if user:
                flash('uid exists.')
                return redirect(url_for('signup'))
            user = Users(
                uid=uid,
                upw=generate_password_hash(upw),
                is_admin=False
            )
            db.session.add(user)
            db.session.commit()
            return redirect(url_for('signin'))
        
        flash('signup fail.')
        return redirect(url_for('signup'))

    elif request.method == 'GET':
        return render_template('signup.html')

@app.route('/user/logout')
@jwt_required()
def logout():
    response = make_response(redirect(url_for('index')))
    response.set_cookie(app.config["JWT_ACCESS_COOKIE_NAME"], '', path='/user', expires=0)
    response.set_cookie('FLAG', '', path='/user', expires=0)
    return response
    

@app.route('/file/<string:user_uuid>/<string:filename>')
def storage(user_uuid, filename):
    if user_uuid and filename:
        files = Storages.query.filter_by(user_uuid=user_uuid, filename=filename).first()
        if files:
            content_type = request.args.get('content-type', 'text/plain')
            response = make_response(files.data)
            response.headers['Content-Type'] = content_type
            return response
        
    return 'Not Found', 404

@app.route('/user')
@jwt_required()
def user():
    files = Storages.query.filter_by(user_uuid=current_user.user_uuid).all()
    return render_template('user/index.html', files=files)

@app.route('/user/upload', methods=['GET', 'POST'])
@jwt_required()
def upload():
    if request.method == 'POST':
        try:
            filename = request.form.get('filename')
            data = request.form.get('data')
            if filename and data:
                newfile = Storages(
                    user_uuid=current_user.user_uuid,
                    filename=filename,
                    data=data
                )
                db.session.add(newfile)
                db.session.commit()
        except DataError:
            flash('File Create fail.')
            return redirect(url_for('upload'))
        return redirect(url_for('user'))

    elif request.method == 'GET':
        return render_template('user/upload.html')

def check_url(url):
    try:
        options = webdriver.ChromeOptions()
        for _ in ['headless', 'window-size=1920x1080', 'disable-gpu', 'no-sandbox', 'disable-dev-shm-usage']:
            options.add_argument(_)
        driver = webdriver.Chrome('./chromedriver', options=options)
        driver.implicitly_wait(3)
        driver.set_page_load_timeout(3)
        
        driver_promise = Promise(driver.get('http://localhost:80/signin'))
        driver_promise.then(driver.find_element_by_name("uid").send_keys(str(ADMIN_USERNAME)))
        driver_promise.then(driver.find_element_by_name("upw").send_keys(str(ADMIN_PASSWORD)))

        driver_promise = Promise(driver.find_element_by_id("submit").click())
        driver_promise.then(driver.get(url))
    except Exception as e:
        driver.quit()
        return False
    finally:
        driver.quit()
    return True

@app.route('/report', methods=['GET', 'POST'])
def report():
    if request.method == 'POST':
        path = request.form.get('path')
        if not path:
            flash('fail.')
            return redirect(url_for('report'))

        if path and path[0] == '/':
            path = path[1:]

        url = f'http://localhost:80/{path}'
        if check_url(url):
            flash('success.')
        else:
            flash('fail.')
        return redirect(url_for('report'))

    elif request.method == 'GET':
        return render_template('report.html')

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=80)

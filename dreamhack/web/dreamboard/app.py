#!/usr/bin/env python3.8
import datetime
import json
import jwt
import re

from sqlalchemy import create_engine
from sqlalchemy.orm import scoped_session
from sqlalchemy.orm import sessionmaker
from sqlalchemy.orm.exc import NoResultFound
from sqlalchemy.ext.declarative import declarative_base

from models import User, Post, Report

from config import *

USER_LEVEL = 1
FLAG_USER_LEVEL = 2
ADMIN_LEVEL = 3
RATE_LIMIT_DELTA = 5 # 5 seconds

engine = create_engine(
	'sqlite:///app.db'
)
Base = declarative_base(engine)
session_factory = sessionmaker(bind=engine)
Session = scoped_session(session_factory)

class SQLAlchemySessionManager:
    def __init__(self, Session):
        self.Session = Session

    def process_resource(self, req, resp, resource, params):
        resource.session = self.Session()

    def process_response(self, req, resp, resource, req_succeeded):
        if hasattr(resource, 'session'):
            resource.session.close()

### FALCON API ###
import falcon
from falcon_auth import FalconAuthMiddleware, JWTAuthBackend
from falcon_cors import CORS

def error(msg):
    return {'error': True, 'msg': msg}

def result(msg):
    return {'error': False, 'msg': msg}


### USER ###
class UserSignInResource:
    def on_post(self, req, resp):
        if 'username' in req.media and 'password' in req.media:
            username = req.media['username']
            password = req.media['password']
            try:
                user = User.find_by_username(self.session, username)
                if user.password != password:
                    raise NoResultFound
                data = {'username': user.username, 'level': user.level, 'exp': 0xffffffff}
                data['user_id'] = user.id
                token = jwt.encode(data, SECRET_KEY).decode('utf-8')
                resp.media = result({'token': token})
                return
            except NoResultFound:
                resp.media = error('login failed')
        else:
            resp.media = error('username, password')

class UserSignUpResource:
    def on_post(self, req, resp):
        if 'username' in req.media and 'password' in req.media:
            username = req.media['username']
            password = req.media['password']
            occupation = req.media['occupation'] if 'occupation' in req.media else 'student' # default value
            try:
                user = User.find_by_username(self.session, username)
                resp.media = error('duplicated username')
            except NoResultFound:
                user = User(username=username,
                            password=password,
                            occupation=occupation,
                            level=USER_LEVEL)
                self.session.add(user)
                self.session.commit()
                resp.media = result('success')
        else:
            resp.media = error('username, password')

### END ###


### POST ###
class PostListResource:
    def on_get(self, req, resp):
        user = req.context['user']
        posts = Post.find_by_author(self.session, user['user_id'])
        rp = [{"title":p.title, "id":p.id} for p in posts]
        resp.media = result(rp)

class PostReadResource:
    def on_get(self, req, resp):
        user = req.context['user']
        if 'id' in req.params:
            post_id = req.params['id']
            try:
                post = Post.find_by_id(self.session, re.match("^\d+", post_id).group(0))
                if post.author == user['user_id'] or\
                        (user['level'] == FLAG_USER_LEVEL and post.author == 1) or user['level'] == ADMIN_LEVEL:
                    rp = {"title": post.title, "content": post.content}
                    resp.media = result(rp)
                else:
                    resp.media = error('permission denied')
            except NoResultFound:
                resp.media = error('no post')
            except:
                resp.media = error('no post')
        else:
            resp.media = error('id')

class PostReportResource:
    def on_get(self, req, resp):
        user = req.context['user']
        if 'id' in req.params:
            post_id = req.params['id']
            try:
                post = Post.find_by_id(self.session, re.match("^\d+", post_id).group(0))
            except NoResultFound:
                resp.media = error('no post')
                return
            except:
                resp.media = error('no post')

            if post.author == user['user_id'] or user['level'] == ADMIN_LEVEL:
                # reporting process
                try:
                    report = Report.find_by_author(self.session, user['user_id'])
                    now = datetime.datetime.utcnow()
                    if (report.report_time + datetime.timedelta(0, RATE_LIMIT_DELTA)).time() > now.time():
                        resp.media = error('rate limit reached')
                        return
                except NoResultFound:
                    pass

                report = Report(post=post_id, author=post.author)
                self.session.add(report)
                self.session.commit()
                resp.media = result('success')
            else:
                resp.media = error('permission denied')
        else:
            resp.media = error('id')


class PostWriteResource:
    def on_post(self, req, resp):
        user = req.context['user']
        if 'title' in req.media and 'content' in req.media:
            title = req.media['title']
            content = req.media['content']
            post = Post(title=title, content=content, author=user['user_id'])
            self.session.add(post)
            self.session.commit()
            resp.media = result('success')
        else:
            resp.media = error('title, content')

### END ###


### ADMIN / USER ###
class ReportListResource:
    def on_get(self, req, resp):
        user = req.context['user']

        if not user['level'] == ADMIN_LEVEL:
            reports = Report.get_reports_by_author_id(self.session, user['user_id'])
            rr = [{"id":r.id, 'post_id': r.post, 'msg': r.msg, 'checked': r.checked} for r in reports]
            resp.media = result(rr)
            return

        reports = Report.get_reports(self.session)
        rr = [{"id":r.id, 'post_id': r.post, 'msg': r.msg} for r in reports]
        resp.media = result(rr)
        return

class ReportCheckResource:
    def on_post(self, req, resp):
        user = req.context['user']
        if not user['level'] == ADMIN_LEVEL:
            resp.media = error('permission denied')
            return

        if 'id' not in req.media or 'msg' not in req.media:
            return error('error1')

        report_id = req.media['id']
        msg = req.media['msg']

        try:
            report = Report.find_by_id(self.session, report_id)
            report.checked = True
            report.msg = msg
            self.session.add(report)
            self.session.commit()
            resp.media = result('success')
        except:
            resp.media = error('no report')


class AdminUserSearchOccupation:
    def on_post(self, req, resp, occupation):
        user = req.context['user']
        if not user['level'] == ADMIN_LEVEL:
            resp.media = error('permission denied')
            return

        users = User.find_by_occupation(self.session, occupation)
        ru = [{"username": u.username, 'level': u.level} for u in users]
        resp.media = result(ru)
        return

class AdminUserSetLevel:
    def on_post(self, req, resp, level):
        user = req.context['user']
        if not user['level'] == ADMIN_LEVEL:
            resp.media = error('permission denied')
            return

        if 'username' not in req.params:
            resp.media = error('no id')
            return

        if int(level) not in [USER_LEVEL, FLAG_USER_LEVEL]:
            resp.media = error('level error')
            return

        username = req.params['username']
        try:
            user = User.find_by_username(self.session, username)
            if user.level == ADMIN_LEVEL:
                resp.media = error('you cannot change admin\'s level')
                return

            user.level = level
            self.session.add(user)
            self.session.commit()
            resp.media = result('success')
        except NoResultFound:
            resp.media = error('no such user')

### END ###

def jwt_loader(data):
    return data

user_loader = jwt_loader
auth_backend = JWTAuthBackend(user_loader, secret_key = SECRET_KEY,
                              required_claims=['exp'])
auth_middleware = FalconAuthMiddleware(auth_backend,
                    exempt_routes=[
                        '/user/token',
                        '/user/signup',
                        ])

### CORS CONFIG ###
cors = CORS(allow_all_origins=True, allow_all_headers=True, allow_all_methods=True)
### END ###

api = falcon.API(middleware=[cors.middleware, auth_middleware, SQLAlchemySessionManager(Session)])

### USER Router ###
api.add_route('/user/token', UserSignInResource()) # POST
api.add_route('/user/signup', UserSignUpResource()) # POST
### END ###

### POST Router ###
api.add_route('/post/list', PostListResource()) # GET
api.add_route('/post/read', PostReadResource()) # GET
api.add_route('/post/write', PostWriteResource()) # POST
api.add_route('/post/report', PostReportResource()) # GET
### END ###

### ADMIN ROUTER ###
api.add_route('/admin/report/list', ReportListResource()) # GET
api.add_route('/admin/report/set', ReportCheckResource()) # POST
api.add_route('/admin/user/get/{occupation}', AdminUserSearchOccupation()) # POST
api.add_route('/admin/user/set/{level}', AdminUserSetLevel()) # POST
### END ###

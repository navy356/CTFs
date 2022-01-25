from flask import Flask, request, jsonify, current_app, send_from_directory
import jwt
import redis
from datetime import timedelta
from time import time
from werkzeug.exceptions import default_exceptions, BadRequest, Unauthorized
from functools import wraps
from json import dumps, loads
from uuid import uuid4
import sys

r = redis.Redis()
app = Flask(__name__)

# SECRET CONSTANTS
# JWT_SECRET = 'JWT_KEY'
# FLAG = 'DH{FLAG_EXAMPLE}'
from secret import JWT_SECRET, FLAG

# PUBLIC CONSTANTS
COUPON_EXPIRATION_DELTA = 45
RATE_LIMIT_DELTA = 10
FLAG_PRICE = 2000
PEPERO_PRICE = 1500


def handle_errors(error):
    return jsonify({'status': 'error', 'message': str(error)}), error.code


for de in default_exceptions:
    app.register_error_handler(code_or_exception=de, f=handle_errors)


def get_session():
    def decorator(function):
        @wraps(function)
        def wrapper(*args, **kwargs):
            uuid = request.headers.get('Authorization', None)
            if uuid is None:
                raise BadRequest("Missing Authorization")

            data = r.get(f'SESSION:{uuid}')
            if data is None:
                raise Unauthorized("Unauthorized")

            kwargs['user'] = loads(data)
            return function(*args, **kwargs)
        return wrapper
    return decorator


@app.route('/flag/claim')
@get_session()
def flag_claim(user):
    if user['money'] < FLAG_PRICE:
        raise BadRequest('Not enough money')

    user['money'] -= FLAG_PRICE
    return jsonify({'status': 'success', 'message': FLAG})


@app.route('/pepero/claim')
@get_session()
def pepero_claim(user):
    if user['money'] < PEPERO_PRICE:
        raise BadRequest('Not enough money')

    user['money'] -= PEPERO_PRICE
    return jsonify({'status': 'success', 'message': 'lotteria~~~~!~!~!'})


@app.route('/coupon/submit')
@get_session()
def coupon_submit(user):
    coupon = request.headers.get('coupon', None)
    if coupon is None:
        raise BadRequest('Missing Coupon')

    try:
        coupon = jwt.decode(coupon, JWT_SECRET, algorithms='HS256')
    except:
        raise BadRequest('Invalid coupon')

    if coupon['expiration'] < int(time()):
        raise BadRequest('Coupon expired!')

    rate_limit_key = f'RATELIMIT:{user["uuid"]}'
    if r.setnx(rate_limit_key, 1):
        r.expire(rate_limit_key, timedelta(seconds=RATE_LIMIT_DELTA))
    else:
        raise BadRequest(f"Rate limit reached!, You can submit the coupon once every {RATE_LIMIT_DELTA} seconds.")


    used_coupon = f'COUPON:{coupon["uuid"]}'
    if r.setnx(used_coupon, 1):
        # success, we don't need to keep it after expiration time
        if user['uuid'] != coupon['user']:
            raise Unauthorized('You cannot submit others\' coupon!')

        r.expire(used_coupon, timedelta(seconds=coupon['expiration'] - int(time())))
        print(coupon['expiration'],file=sys.stderr)
        print(timedelta(seconds=coupon['expiration'] - int(time())), file=sys.stderr)
        user['money'] += coupon['amount']
        r.setex(f'SESSION:{user["uuid"]}', timedelta(minutes=10), dumps(user))
        return jsonify({'status': 'success'})
    else:
        # double claim, fail
        raise BadRequest('Your coupon is alredy submitted!')


@app.route('/coupon/claim')
@get_session()
def coupon_claim(user):
    if user['coupon_claimed']:
        raise BadRequest('You already claimed the coupon!')

    coupon_uuid = uuid4().hex
    data = {'uuid': coupon_uuid, 'user': user['uuid'], 'amount': 1000, 'expiration': int(time()) + COUPON_EXPIRATION_DELTA}
    uuid = user['uuid']
    user['coupon_claimed'] = True
    coupon = jwt.encode(data, JWT_SECRET, algorithm='HS256')
    r.setex(f'SESSION:{uuid}', timedelta(minutes=10), dumps(user))
    return jsonify({'coupon': coupon})


@app.route('/session')
def make_session():
    uuid = uuid4().hex
    r.setex(f'SESSION:{uuid}', timedelta(minutes=10), dumps(
        {'uuid': uuid, 'coupon_claimed': False, 'money': 0}))
    return jsonify({'session': uuid})


@app.route('/me')
@get_session()
def me(user):
    return jsonify(user)


@app.route('/')
def index():
    return current_app.send_static_file('index.html')

@app.route('/images/<path:path>')
def images(path):
    return send_from_directory('images', path)

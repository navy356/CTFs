from .. import models
import json
import jsonpickle
from flask_restful import abort, reqparse


parser = reqparse.RequestParser()
parser.add_argument('X-PubKey', location='headers')

def require(assertion, abort_code=401, message=None):
    if not assertion:
        abort(abort_code, message=message, osd={
            'type': 'error',
            'message': message
        })

def strip(string, chars, strip_all=True):
    for c in chars:
        string = string.replace(c, '', strip_all)
    return string

def get_current_user():
    args = parser.parse_args()
    if args['X-PubKey']:
        return models.User.get(pubkey=args['X-PubKey'])

def require_loggedin(fn):
    def wrapped(*args, **kwargs):
        require(get_current_user())
        return fn(*args, **kwargs)
    return wrapped

def require_admin(fn):
    def wrapped(*args, **kwargs):
        curr_user = get_current_user()

        require(curr_user)
        require(curr_user.user_type.id == models.USER_TYPE_ADMIN.id)

        return fn(*args, **kwargs)
    return wrapped

def jsonify(fn):
    def wrapped(*args, **kwargs):
        ret = fn(*args, **kwargs)
        if type(ret) is not tuple:
            ret = (ret,)
        result = (json.loads(jsonpickle.encode(ret[0], unpicklable=False)),) + ret[1:]
        result = result[0] if len(result) == 1 else result
        return result
    return wrapped

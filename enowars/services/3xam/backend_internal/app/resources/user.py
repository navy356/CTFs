from . import require, get_current_user, \
        require_loggedin, jsonify, parser
from .. import models
from .. import measure
from flask import request
from flask_restful import Resource, abort

MESSAGE_ACCOUNT_CREATED = 'Welcome, {user.name}!'


class UserResource(Resource):
    @measure('UserResource::get')
    @jsonify
    @require_loggedin
    def get(self, user_id):
        args = parser.parse_args()
        curr_user = get_current_user()

        if user_id == 'current':
            user = curr_user
        else:
            user_id = int(user_id)

            user = models.User.get(id=user_id)
            require(user, 404)

            if user.user_type.id != models.USER_TYPE_NORMAL.id:
                require(curr_user.user_type.id == models.USER_TYPE_ADMIN.id,
                        401, "Not enough permissions to access specified user")

        return {'user': user}

    @measure('UserResource::post')
    @jsonify
    def post(self):
        curr_user = get_current_user()
        args = parser.parse_args()
        user_type_id = models.USER_TYPE_NORMAL
        if curr_user:
            # only admin can add other users and specify their user_type
            require(curr_user.user_type.id == models.USER_TYPE_ADMIN.id,
                    401, 'Only admin can add other users')
            if 'user_type_id' in request.json:
                user_type_id = request.json['user_type_id']
            pubkey = request.json['pubkey']
        else:
            pubkey = args['X-PubKey']

        require(pubkey, 400, 'Missing pubkey field')
        require('name' in request.json, 400, 'Missing name field')
        require(request.json['name'], 400, 'Must specify name')
        require(type(request.json['name']) is str, 400, 'Name must be string')

        u = models.User(
                name=request.json['name'],
                user_type=user_type_id,
                pubkey=pubkey)
        require(u.save(), 500, 'Failed to save user')

        u = models.User.get(id=u.id) # to populate any default fields

        return {'user': u, 'osd': {'message':
            MESSAGE_ACCOUNT_CREATED.format(user=u)}}

    @measure('UserResource::put')
    @jsonify
    @require_loggedin
    def put(self, user_id):
        require('name' in request.json, 400, 'Missing name')
        require(request.json['name'], 400, 'Must specify name')
        require(type(request.json['name']) is str, 400, 'Name must be string')

        curr_user = get_current_user()

        if user_id != 'current':
            user_id = int(user_id)
            target_user = models.User.get(id=user_id)
            require(target_user, 400, 'User not found')
        else:
            target_user = curr_user

        require(target_user == curr_user, 401, 'Can only edit own user')

        target_user.name = request.json['name']
        require(target_user.save(), 400, 'An error occured while saving')

        return {'user': target_user,
                'osd': {'message': 'User updated successfully'}}

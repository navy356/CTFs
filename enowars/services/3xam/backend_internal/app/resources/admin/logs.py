from .. import jsonify, require_admin, require
from ... import measure
from flask_restful import Resource
from flask import request
import requests


class AdminLogsResource(Resource):
    @measure('AdminLogsResource::get')
    @require_admin
    def get(self):
        tag = request.args.get('tag')
        match = request.args.get('match') or ''
        match = match.strip()
        if tag:
            tag = tag.strip()
            if not tag.startswith('/'):
                tag = f'/{tag}'
        resp = requests.get(f'http://logger/backend_internal{tag}?match={match}')
        return resp.json()



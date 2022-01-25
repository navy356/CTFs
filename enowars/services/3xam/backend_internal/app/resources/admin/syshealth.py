from .. import require_admin, require
from ... import measure
from flask_restful import Resource
from flask import request
import logging
import shutil
import psutil


logger = logging.getLogger(__name__)

class AdminSystemHealthResource(Resource):
    @measure('AdminSystemHealthResource::post')
    @require_admin
    def post(self):
        require('check_id' in request.json, 400)
        check_id = request.json['check_id']
        logger.warn(f'Initiating system health check with id: {check_id}')

        total, used, free = shutil.disk_usage("/")

        result = {
            'disk': {
                'total': total // (2**30),
                'used': used // (2**30),
                'free': free // (2**30)
            },
            'mem': {
                'total': psutil.virtual_memory()[0],
                'used': psutil.virtual_memory()[3],
                'free': psutil.virtual_memory()[4],
            }
        }
        logger.info('System check complete')
        return result


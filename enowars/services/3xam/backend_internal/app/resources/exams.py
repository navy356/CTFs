from . import jsonify, require_loggedin
from .. import models
from .. import measure
from flask_restful import Resource


class ExamsResource(Resource):
    @measure('ExamsResource:get')
    @jsonify
    @require_loggedin
    def get(self):
        return {'exams': models.Exam().find()}

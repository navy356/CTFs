from . import jsonify, require_loggedin, require
from .. import models
from .. import measure
from flask_restful import Resource
from flask import request


class ScoresResource(Resource):
    PAGE_SIZE = 20
    @measure('ScoresResource::get')
    @jsonify
    @require_loggedin
    def get(self, exam_id):
        exam = models.Exam.get(id=exam_id)
        require(exam, 404)
        total_scores = models.Score.count(exam=exam)
        count = request.args.get('count') or self.PAGE_SIZE
        offset = request.args.get('offset') or 0

        output = {'exam': exam.to_dict()}
        scores = [s.to_dict() for s in models.Score.find(offset=offset,
            limit=count, exam=exam, order=1)]

        for s in scores:
            s['user'] = {'name': models.User.get(id=s['user_id']).name}

        output['scores'] = scores
        output['scores_count'] = total_scores

        return output


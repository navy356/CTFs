from . import jsonify, require_admin, require
from .. import models
from .. import measure
from flask_restful import Resource
from flask import request


class AnswersResource(Resource):
    @measure('AnswersResource::post')
    @jsonify
    @require_admin
    def post(self, question_id):
       require(models.Question.get(id=question_id), 404, 'No such question')
       data = request.json

       require('answer' in data, 400)
       data = data['answer']

       require('value' in data and type(data['value']) is str, 400)

       id = data['id'] if 'id' in data else None
       index = data['index'] if 'index' in data else 0

       a = models.Answer(id=id, question=question_id,
               value=data['value'], index=index)

       require(a.save(), 500, 'An error occured while saving')
       return { 'answer': a, 'osd': { 'message': 'The answer was added' }}


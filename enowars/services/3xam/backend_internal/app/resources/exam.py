from . import jsonify, require_loggedin
from .. import models
from .. import measure
from flask_restful import Resource, abort
from flask import request


MESSAGE_EXAM_STARTED = 'You have started the exam. Good luck!'

class ExamResource(Resource):
    @measure('ExamResource::get')
    @jsonify
    @require_loggedin
    def get(self, exam_id):
        exam = models.Exam.get(id=exam_id)
        if exam is None:
            abort(404)
        return {'exam': exam, 'number_of_questions': len(exam.questions)}

    @measure('ExamResource::post')
    @jsonify
    @require_loggedin
    def post(self, exam_id):
        exam = models.Exam.get(id=exam_id)
        if exam is None:
            abort(404)

        questions = models.Question.find(exam=exam)
        if not len(questions):
            abort(404, message='The exam does not have any questions')

        return {
            'next_question_hash': questions[0].hash,
            'osd': {'message': MESSAGE_EXAM_STARTED, 'type': 'info'}
        }

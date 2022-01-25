from . import jsonify, require_admin, require
from .. import models
from .. import measure
from flask_restful import Resource


class QuestionsResource(Resource):
    @measure('QuestionsResource::get')
    @require_admin
    def get(self, question_id, answer_id=None):
        if answer_id is not None:
            answers = models.Answer.find(question=question_id, id=answer_id)
            require(len(answers), 404, 'No such question')
            q = answers[0].question
        else:
            q = models.Question.get(id=question_id)
            answers = q.answers
        data = q.to_dict()
        data['answers'] = [a.to_dict() for a in answers]
        return { 'question': data }

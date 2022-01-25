from . import jsonify, require_loggedin, require, \
        get_current_user, strip
from .. import models
from .. import measure
from flask_restful import Resource, abort
from flask import request
from werkzeug.exceptions import HTTPException
import hashlib
import math
from functools import reduce


MESSAGE_SUBMISSION = 'Your answer: {submission.value} {message}'
MESSAGE_SUBMISSION_CORRECT = 'is correct. {submission.points} points earned'
MESSAGE_SUBMISSION_INCORRECT = 'is wrong. Lost {submission.points} points :('

class ExamQuestionsResource(Resource):
    @measure('ExamQuestionsResource::get')
    @jsonify
    @require_loggedin
    def get(self, exam_id, question_hash):
        questions = models.Question.find(exam=exam_id)
        for i in range(0, len(questions)):
            q = questions[i]
            if q.hash == question_hash:
                data = q.to_dict()
                return {'question': data, 'question_index': i}
        abort(404)

    @measure('ExamQuestionsResource::post')
    @jsonify
    @require_loggedin
    def post(self, exam_id, question_hash):
        questions = models.Question.find(exam=exam_id)
        user = get_current_user()
        data = request.json

        require(len(questions), 404, 'Question not found')
        require('answer' in data, 400, 'Answer not specified')
        require(type(data['answer']) is str, 400, 'Unexpected answer type')

        question_index, question = next((i, v) for i,v in enumerate(questions)
                if v.hash == question_hash)

        submission = models.Submission.get(user=user, question=question)
        if submission is None:
            submission = models.Submission()

        # kill xss and sql injections
        submission.value = strip(data['answer'], ' <>+-%{}[].!@#$%^&*()|\'"`~,;\r\n')
        submission.points = question.points
        submission.user = user
        submission.question = question

        # prep osd message
        success_message = MESSAGE_SUBMISSION.format(message=MESSAGE_SUBMISSION_CORRECT,
                submission=submission)
        fail_message = MESSAGE_SUBMISSION.format(message=MESSAGE_SUBMISSION_INCORRECT,
                submission=submission)

        correct_answers = map(lambda a: a.value,  models.Answer.find(question=question))
        correct_answer = False
        osd_message = fail_message

        try:
            hashed_value = hashlib.sha256(
                    models.ANSWERS_SALT + submission.value.encode()).hexdigest()
            if hashed_value in correct_answers:
                require(submission.save(), 400, 'Error saving submission')
                correct_answer = True
                osd_message = success_message
            success = True
        except HTTPException:
            # let through exceptions triggered by any require
            raise
        except:
            # save might raise an exception if it fails
            success = False

        require(success, 400, 'Error processing submission')

        result = {
            'osd': {
                'type': 'success' if correct_answer else 'error',
                'message': osd_message.format(submission=submission)
            }
        }

        if question_index + 1 < len(questions):
            result['next_question_hash'] = questions[question_index + 1].hash
        else:
            # this was the last question, compute final score
            submissions = map(lambda q: models.Submission.get(user=user,
                question=q), questions)
            achievable_points = reduce(lambda a, b: a + b,
                map(lambda q: q.points, questions)
            )
            points = [s.points for s in submissions if s]
            total = reduce(lambda a, b: a + b, points) if len(points) else 0
            require(models.Score(user=user,
                exam=exam_id, score=math.ceil((total / achievable_points) * 100)).save(),
                    500, 'An error occured while saving score')
        return result, (200 if correct_answer else 202)


from flask import Flask
from flask_restful import Api 
from app.orm import model
from app import models
from app import resources
from app.resources import answers, exam, exam_questions, exams, questions,\
user, scores
from app.resources.admin import logs, syshealth
import mysql.connector
import os
from multiprocessing import Lock

app = Flask(__name__)
api =  Api(app)

print('Initializing DB connection')
model.Model.connection = mysql.connector.connect(
    host=os.environ['DB_HOST'],
    user=os.environ['DB_USERNAME'],
    password=os.environ['DB_PASSWORD'],
    database=os.environ['DB_NAME'])

cursor = model.Model.connection.cursor()
cursor.execute('SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED;')
cursor.close()

print('Initializing REST resources')
api.add_resource(user.UserResource, '/users/<string:user_id>', '/users')
api.add_resource(exam.ExamResource, '/exams/<int:exam_id>')
api.add_resource(scores.ScoresResource, '/exams/<int:exam_id>/scores')
api.add_resource(exams.ExamsResource, '/exams')
api.add_resource(exam_questions.ExamQuestionsResource,
        '/exams/<int:exam_id>/<string:question_hash>')
api.add_resource(questions.QuestionsResource, '/questions/<int:question_id>',
        '/questions/<int:question_id>/answers/<int:answer_id>')
api.add_resource(answers.AnswersResource,
        '/questions/<int:question_id>/answers')
api.add_resource(logs.AdminLogsResource, '/admin/logs')
api.add_resource(syshealth.AdminSystemHealthResource,
        '/admin/syshealth')

l = Lock()
l.acquire(block=True)
print('Checking whether to insert seed data')
if not models.User.get(id=1):
    print('No users in DB, inserting seed data')
    for item in models.SEED:
        assert item.save(), "Failed to save %s" % item
else:
    print('No need to insert seed data')
l.release()

if __name__ == '__main__':
    print('Executing App')
    app.run(debug=True, port=9000)


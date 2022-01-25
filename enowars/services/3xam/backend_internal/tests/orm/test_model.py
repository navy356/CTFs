from app import models
from app.orm import model
import mysql.connector
import pytest
import time

connection_data = {
    'host': 'localhost',
    'user': 'root',
    'password': '123456',
}

SEED = [
    models.USER_TYPE_ADMIN, models.USER_TYPE_NORMAL,
    models.USER_TYPE_TEACHER,
    models.Exam(id=1, description='exam_1'),
    models.Exam(id=2, description='exam_2'),
    models.Question(1, 'safsadfgsag', 1, 0, 'exam_1_question_1', 10),
    models.Question(2, '', 1, 1, 'exam_1_question_2', 20),
    models.Question(3, 'fasgfasgd', 1, 1, 'exam_1_question_3', 30),
]

@pytest.fixture(autouse=True)
def prep():
    connection = mysql.connector.connect(**connection_data)
    with connection.cursor() as cursor:
        with open('sql/empty_db.sql', 'r') as f:
            data = f.read()
            cursor.execute(data, multi=True)
    connection.close()
    time.sleep(0.1)
    connection = mysql.connector.connect(**connection_data, database='3xam')
    model.Model.connection = connection

    for s in SEED:
        assert s.save()
    yield

    connection.close()

def test_user_add():
    user_list = [
        ('xxname1', 'key1', True),
        ('xxname2', 'key2', True),
        ('xxname3', 'key1', False),
        ('xxname2', 'key4', True),
    ]
    for u in user_list:
        name, pubkey, result = u
        model = models.User(name=name, pubkey=pubkey)
        result = model.save()
        if result:
            assert model.id is not None
            assert model.id != 0
            assert model.name == name
            assert model.pubkey == pubkey
        else:
            assert not model.id

def test_user_get():
    user = models.User(name='name1', pubkey='key1')
    assert user.save()
    assert user.id != 0
    assert models.User.get(pubkey='key1') == models.User(
        id=user.id,
        user_type=3,
        name='name1',
        pubkey='key1'
    )

    assert models.User.get(pubkey='nonexistent') is None

def test_exams_get():
    exams = models.Exam.find()
    assert len(exams) == 2
    assert exams[0] == models.Exam(id=1, description='exam_1')
    assert exams[1] == models.Exam(id=2, description='exam_2')

def test_exam_get():
    assert models.Exam.get(id=1) == models.Exam(id=1, description='exam_1')
    assert models.Exam.get(id=2) == models.Exam(id=2, description='exam_2')
    assert models.Exam.get(id=55) is None

def test_questions_get():
    questions = models.Question.find(exam=1)
    expected = [
        models.Question(1, 'safsadfgsag', 1, 0, 'exam_1_question_1', 10),
        models.Question(2, '', 1, 1, 'exam_1_question_2', 20),
        models.Question(3, 'fasgfasgd', 1, 1, 'exam_1_question_3', 30),
    ]
    assert len(questions) == len(expected)

    for i in range(0, len(expected)):
        assert questions[i] == expected[i], 'question %d does not match' % i

def test_submissions_add_valid():
    user = models.User(name='name1', pubkey='key1')
    assert user.save()
    expected = models.Submission(1, user.id, 1, 'q1value')

    actual = models.Submission(user=user.id, question=1, value='q1value')
    actual.save()
    assert expected == actual

def test_submission_add_invaliduser():
    submission = models.Submission(user=55, question=1, value='q1value')
    assert not submission.save()
    assert submission.id is None

def test_submission_add_invalidquestion():
    submission = models.Submission(user=1, question=55, value='q1value')
    assert not submission.save()
    assert submission.id is None

from .orm.model import *
import os
import hashlib

SEED = []
ANSWERS_SALT = b'ENOWARS_SALT'

class UserType(Model):
    name = TextField()

class User(Model):
    user_type = ForeignKeyField(UserType)
    name = TextField()
    pubkey = TextField()

class Exam(Model):
    description = TextField()

class Question(Model):
    hash = TextField()
    exam = ForeignKeyField(Exam)
    type = IntegerField()
    text = TextField()
    points = IntegerField()

class Answer(Model):
    value = TextField()
    question = ForeignKeyField(Question)
    index = IntegerField()

class Submission(Model):
    user = ForeignKeyField(User)
    question = ForeignKeyField(Question)
    value = TextField()
    points = IntegerField()

class Score(Model):
    user = ForeignKeyField(User)
    exam = ForeignKeyField(Exam)
    score = IntegerField()

USER_TYPE_ADMIN = UserType(id=1, name='admin')
USER_TYPE_TEACHER = UserType(id=2, name='teacher')
USER_TYPE_NORMAL = UserType(id=3, name='normal')

# Add user types
SEED.extend([USER_TYPE_ADMIN, USER_TYPE_TEACHER, USER_TYPE_NORMAL])


# Add users
SEED.extend([
    User(id=1, name='admin', user_type=USER_TYPE_ADMIN,
         pubkey='cBjEl+JgQG9tsngU3ieItjg360I8VSkB+YOUbp3A3yY='),
    User(id=2, name='tgalal', user_type=USER_TYPE_TEACHER,
         pubkey='dC92Kc9f1yOJVooNO3wwChK1c4u7RjjOzh9qJMZAUyc='),
    User(id=3, name='checker', user_type=USER_TYPE_NORMAL,
         pubkey='tqnAETXu/GZYaZ11bF/voku/mX49A4Cq1wFBVNv4OVE='),
   ])

# Add exams
SEED.extend([
    Exam(id=1, description='Number Sequences'),
    ])

# Add questions
SEED.extend([
    Question(
        id=1,
        exam=1,
        type=1,
        hash=hashlib.sha256(os.urandom(8)).hexdigest(),
        text='What comes next in the sequence 2, 4, 8, 16, ?',
        points=1,
    ),
    Answer(question=1, index=1000,
        value='591c15930935d8e221fc15e22807ee14b457c9526c7678c44e2b9617587f63e7'),
    Question(
        id=2,
        exam=1,
        type=1,
        hash=hashlib.sha256(os.urandom(8)).hexdigest(),
        text='What comes next in the sequence 911, 909, 907, 906, 905, 904, 901, 900, ?',
        points=20,
    ),
    Answer(question=2, index=1000,
        value='1e149d4d805cb57b7d18e43380e8fbf3b4f5c623c745ac7b1c48d92bf542f45a'),
    Question(
        id=3,
        exam=1,
        type=1,
        hash=hashlib.sha256(os.urandom(8)).hexdigest(),
        text='What comes next in the sequence 2032, 2030, 2006, 2004, 2002, 2000, ?"',
        points=30,
    ),
    Answer(question=3, index=1000,
        value='3862fb549cd36c5e51c8de46b92c1fff0ac01e353fc68f2b14b585fe7ea4de44'),
    Question(
        id=4,
        exam=1,
        type=1,
        hash=hashlib.sha256(os.urandom(8)).hexdigest(),
        text='What comes next in the sequence 307, 306, 305, 303, 300, ?"',
        points=30,
    ),
    Answer(question=4, index=1000,
        value='4c93ff91769280d577c4b7e0c18556e0ce3d38172c6b522b6cef2730c4cb3fa8'),
    Question(
        id=5,
        exam=1,
        type=1,
        hash=hashlib.sha256(os.urandom(8)).hexdigest(),
        text='What comes next in the sequence 400, 111, 109, 107, 106, 105, 104, 101, 100, ?"',
        points=30,
    ),
    Answer(question=5, index=1000,
        value='148dd8bdd174608f0f0900cb16b446e825ecb0498812401805aded13f6efd7e4'),
])

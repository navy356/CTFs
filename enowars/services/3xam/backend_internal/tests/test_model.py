from app.orm import model
from app.orm import fields
import pytest

class UserType(model.Model):
    name = fields.TextField()

class User(model.Model):
    user_type = fields.ForeignKeyField(UserType)
    firstname = fields.TextField()
    lastname = fields.TextField()
    age = fields.IntegerField()

@pytest.fixture(autouse=True)
def prep():
    model.Model.connection = None
    yield

@pytest.mark.parametrize('model,expected', [
        (User(firstname='fname', lastname='lname', age=20),
        (
            'INSERT INTO users (`firstname`, `lastname`, `age`) VALUES (%s, %s, %s)',
            ('fname', 'lname', 20)
        )),
        (User(firstname='fname', age=5),
        (
            'INSERT INTO users (`firstname`, `age`) VALUES (%s, %s)',
            ('fname', 5)
        )),
        (User(id=10, firstname='fname', age=5),
        (
            'UPDATE users SET `id` = %s, `firstname` = %s, `age` = %s WHERE `id` = %s',
            (10, 'fname', 5, 10)
        )),
    ])
def test_save(model, expected):
    result = model.save()
    assert result[0] == expected[0], result[0]
    assert result[1] == expected[1], result[1]


@pytest.mark.parametrize('actual,expected', [
    (User.find(), (
        'SELECT `id`, `user_type_id`, `firstname`, `lastname`, `age` FROM users WHERE 1 = 1',
        ()
    )),
    (User.find(['firstname']), (
        'SELECT `firstname` FROM users WHERE 1 = 1',
        ()
    )),
    (User.find(id=5), (
        'SELECT `id`, `user_type_id`, `firstname`, `lastname`, `age` FROM users WHERE `id` = %s',
        (5,)
    )),
    (User.find(['id'], user_type=5), (
        'SELECT `id` FROM users WHERE `user_type_id` = %s',
        (5,)
    )),
    (User.find(['id'], user_type=UserType(5)), (
        'SELECT `id` FROM users WHERE `user_type_id` = %s',
        (5,)
    )),
    (User.find(['firstname', 'lastname'], id=5), (
        'SELECT `firstname`, `lastname` FROM users WHERE `id` = %s',
        (5,)
    )),
    (User.find(firstname='fname', lastname='lname'), (
        'SELECT `id`, `user_type_id`, `firstname`, `lastname`, `age` FROM users WHERE `firstname` = %s \
AND `lastname` = %s',
        ('fname', 'lname')
    )),
    (User.find(['id'], user_type=(1,2)), (
        'SELECT `id` FROM users WHERE `user_type_id` IN (%s, %s)',
        (1,2)
    )),
    (User.find(['id'], user_type=(UserType(1), UserType(2))), (
        'SELECT `id` FROM users WHERE `user_type_id` IN (%s, %s)',
        (1,2)
    )),
    (User.find(['id'], user_type=()), (
        'SELECT `id` FROM users WHERE 1 = 0',
        ()
    )),
    ])
def test_find(actual, expected):
    assert actual == expected, actual

@pytest.mark.parametrize('data,expected', [
    ({'a': 1, 'b': 2}, ('a = %s AND b = %s', (1, 2))),
    ({'b': 3, 'a': (1, 2)}, ('b = %s AND a IN (%s, %s)', (3, 1, 2))),
    ({'b': (3, 4), 'a': (1, 2)}, ('b IN (%s, %s) AND a IN (%s, %s)', (3, 4, 1, 2))),
    ])
def test_conds_to_sql(data, expected):
    assert model.Model.conds_to_sql(**data) == expected

@pytest.mark.parametrize('actual,expected', [
    (User.tablename(), 'users'),
    (UserType.tablename(), 'user_types')
    ])
def test_tablename(actual, expected):
    assert actual == expected

@pytest.mark.parametrize('actual,expected', [
    (UserType(200).users, ('SELECT `id`, `user_type_id`, `firstname`, `lastname`, \
`age` FROM users WHERE `user_type_id` = %s', (200,))),
    (UserType(300).users, ('SELECT `id`, `user_type_id`, `firstname`, `lastname`, \
`age` FROM users WHERE `user_type_id` = %s', (300,))),
    ])
def test_foreign_get(actual, expected):
    assert actual == expected

def test_to_dict():
    expected = {'id': 5, 'age': 20, 'user_type_id': 4}
    actual = User(id=5, age=20, user_type=UserType(4)).to_dict()
    assert expected == actual

@pytest.mark.parametrize('actual,expected', [
    (User.count(), ('SELECT COUNT(*) FROM users WHERE (1 = 1)', ())),
    (UserType.count(), ('SELECT COUNT(*) FROM user_types WHERE (1 = 1)', ())),
    (User.count(user_type=4),
        ('SELECT COUNT(*) FROM users WHERE (`user_type_id` = %s)', (4,))),
    ])
def test_count(actual, expected):
    assert actual == expected

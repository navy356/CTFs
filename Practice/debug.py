from flask import Flask, session, render_template, request, Response, render_template_string, g
import functools, sqlite3, os

app = Flask(__name__)
app.config['SECRET_KEY'] = os.urandom(120)

acc_tmpl = '''{% extends 'index.html' %}
{% block content %}
<h3>baby_ninja joined, total number of rebels: reb_num<br>
{% endblock %}
'''

def get_db():
    db = getattr(g, '_database', None)
    if db is None:
        db = g._database = sqlite3.connect('/tmp/ninjas.db')
        db.isolation_level = None
        db.row_factory = sqlite3.Row
        db.text_factory = (lambda s: s.replace('{{', '').
            replace("'", '&#x27;').
            replace('"', '&quot;').
            replace('<', '&lt;').
            replace('>', '&gt;')
        )
    return db

def query_db(query, args=(), one=False):
    with app.app_context():
        cur = get_db().execute(query, args)
        rv = [dict((cur.description[idx][0], str(value)) \
            for idx, value in enumerate(row)) for row in cur.fetchall()]
        return (rv[0] if rv else None) if one else rv

@app.before_first_request
def init_db():
    with app.open_resource('schema.sql', mode='r') as f:
        get_db().cursor().executescript(f.read())

@app.teardown_appcontext
def close_connection(exception):
    db = getattr(g, '_database', None)
    if db is not None: db.close()

def rite_of_passage(func):
    @functools.wraps(func)
    def born2pwn(*args, **kwargs):

        name = request.args.get('name', '')

        if name:
            query_db('INSERT INTO ninjas (name) VALUES ("%s")' % name)

            report = render_template_string(acc_tmpl.
                replace('baby_ninja', query_db('SELECT name FROM ninjas ORDER BY id DESC', one=True)['name']).
                replace('reb_num', query_db('SELECT COUNT(id) FROM ninjas', one=True).itervalues().next())
            )

            if session.get('leader'): 
                return report

            return render_template('welcome.jinja2')
        return func(*args, **kwargs)
    return born2pwn

@app.route('/')
@rite_of_passage
def index():
    return render_template('index.html')

@app.route('/debug')
def debug():
    return Response(open(__file__).read(), mimetype='text/plain')

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=1337, debug=True)
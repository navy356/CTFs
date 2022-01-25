from flask import (
    Flask,
    request,
    render_template,
    make_response,
    redirect,
    url_for,
    session,
    g,
)
from flask_sqlalchemy import SQLAlchemy
import hashlib, redis, random, os, json
from utils import send_mail, FLAG, SECRET

app = Flask(__name__)
app.secret_key = os.urandom(32)

REDIS_HOST = "127.0.0.1"
conn = redis.Redis(host=REDIS_HOST, charset="utf-8", decode_responses=True)

app.config["SQLALCHEMY_DATABASE_URI"] = "sqlite:///db.db"
db = SQLAlchemy(app)

class User(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    userid = db.Column(db.String(80), unique=True)
    password = db.Column(db.String(80))
    email = db.Column(db.String(80))
    auth = db.Column(db.Boolean())

    def __init__(self, userid, password, email):
        self.userid = userid
        self.password = password
        self.email = email
        self.auth = False

    def to_json(self):
        return {
            "userid": self.userid.lower(),
            "password": self.password.lower(),
            "email": self.email.lower(),
            "auth": self.auth,
        }

db.create_all()

# usr = User(userid='admin', password=hashlib.md5(util.SECRET).hexdigest(), email=f'admin@ctfprob.dreamhack.io')
# db.session.add(usr)
# db.session.commit()


class Auth:
    def __init__(self, email):
        self.email = email
        self.emailauth_key = f"auth:{self.email}"
        self.getcount_key = f"auth_count:{self.email}"

    def set(self):
        setcount = conn.get(self.email)
        if setcount:
            setcount = int(setcount) + 1
        else:
            setcount = 1
        if setcount < 5:
            self.random_key = f"{random.randint(111111, 999999):04d}"
            conn.set(self.emailauth_key, self.random_key)
            conn.set(self.getcount_key, "0")
            conn.set(self.email, setcount)
            return True
        else:
            return False

    def get(self):
        getcount = conn.get(self.getcount_key)
        if getcount:
            getcount = int(getcount) + 1
        else:
            getcount = 1
        if getcount < 5:
            conn.set(self.getcount_key, getcount)
            return conn.get(self.emailauth_key)
        else:
            return None


def is_login():
    return session.get("info")


def get_userid():
    return session.get("info")["userid"]


def get_useremail():
    return session.get("info")["email"]


def alert(msg, loc=None):
    if loc:
        return f'<script>alert("{msg}");location.href="{loc}";</script>'
    else:
        return f'<script>alert("{msg}");history.back(1);</script>'


def verify_uid(userid):
    for banword in ["@", " "]:
        if banword in userid:
            return False
    return True


@app.route("/", methods=["GET", "POST"])
def index():
    if is_login():
        info = json.dumps(session["info"], indent=4)
    else:
        info = "No Info"
    return render_template("index.html", info=info)


@app.route("/flag")
def flag():
    if not is_login():
        return alert("No Signin!")
    myusr = User.query.filter_by(userid=get_userid()).first()
    if myusr.userid == "admin" and myusr.auth:
        return render_template("flag.html", flag=FLAG)
    return alert("No Flag")


@app.route("/signup", methods=["GET", "POST"])
def signup():
    if is_login():
        return alert("Already Signin!", "/")
    if request.method == "GET":
        return render_template("signup.html")
    elif request.method == "POST":
        userid = request.form.get("userid", "")
        userpw = request.form.get("userpw", "")
        if not verify_uid(userid):
            return alert("Check userid")
        try:
            usr = User(
                userid=userid,
                password=hashlib.md5(userpw.encode()).hexdigest(),
                email=f"{userid}@ctfprob.dreamhack.io",
            )
            db.session.add(usr)
            db.session.commit()
        except:
            return alert("Already Signup")
        session["info"] = usr.to_json()
        return alert("Signup!", "/")


@app.route("/signin", methods=["GET", "POST"])
def signin():
    if is_login():
        return alert("Already Signin!", "/")
    if request.method == "GET":
        return render_template("signin.html")
    elif request.method == "POST":
        userid = request.form.get("userid", "")
        userpw = request.form.get("userpw", "")
        usr = User.query.filter_by(
            userid=userid, password=hashlib.md5(userpw.encode()).hexdigest()
        ).first()
        if not hasattr(usr, "userid"):
            return alert("Check userid or userpw")
        session["info"] = usr.to_json()
        return alert("Signin!", "/")


@app.route("/signout", methods=["GET", "POST"])
def signout():
    if not is_login():
        return alert("No Signin!", "/")
    del session["info"]
    return alert("Signout!", "/")


@app.route("/email_verify", methods=["GET", "POST"])
def email_verify():
    if not is_login():
        return alert("No Signin!", "/")
    email = get_useremail()
    if request.method == "GET":
        return render_template("email_verify.html", email=email)
    elif request.method == "POST":
        auth = Auth(email)
        if not auth.set():
            return alert("Too many Auth")
        if send_mail(email, auth.random_key):
            return alert(f"Send Email To {email}", "/email_verify_chk")
        else:
            return alert("Send Email Err")


@app.route("/email_verify_chk", methods=["GET", "POST"])
def email_verify_chk():
    if not is_login():
        return alert("No Signin!", "/")
    email = get_useremail()
    if request.method == "GET":
        return render_template("email_verify_chk.html", email=email)
    elif request.method == "POST":
        auth = Auth(email)
        authcode = auth.get()
        if not authcode:
            return alert("Too many Auth or No Code Generated")
        else:
            code = request.form.get("code", "")
            if code == authcode:
                usr = User.query.filter_by(userid=get_userid()).first()
                usr.auth = True
                db.session.commit()
                session["info"] = usr.to_json()
                return alert("AUTH!")
            else:
                return alert("Check Code")


app.run(host="0.0.0.0", port=5000, threaded=True)
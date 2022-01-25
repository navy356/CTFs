from os import urandom
from hashlib import sha256
from flask import Flask, request, jsonify, send_from_directory
from flask_cors import CORS
from flask_jwt_extended import (
    JWTManager,
    jwt_required,
    create_access_token,
    get_jwt_identity,
)
import pyotp
from models import Users, OTPStorages, init_db

app = Flask(__name__, static_url_path="")
app.config["SQLALCHEMY_DATABASE_URI"] = "sqlite:///supersecureotp.db"
app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = False
app.secret_key = urandom(32)  # random 32 bytes key
db = init_db(app)
jwt = JWTManager(app)
CORS(app, resources={r"/*": {"origins": "*"}})
OTP_VALID_SEC = 120  # 2 minutes


@app.route("/", defaults={"path": ""})
@app.route("/<string:path>")
@app.route("/<path:path>")

def index(path):
    return send_from_directory(app.static_folder, "index.html")


@app.route("/signup", methods=["POST"])
def signup():
    if request.method == "POST":
        userid = request.json["userid"]
        userpw = request.json["userpw"]
        if userid and userpw:
            user = Users.query.filter_by(userid=userid).first()
            if len(userid) < 4 or len(userpw) < 4:
                return jsonify({"result": False, "msg": "ID 또는 PW가 4자 이상인지 확인해주세요."})
            if user:
                return jsonify({"result": False, "msg": "이미 가입된 이용자 입니다."})
            m = sha256()
            m.update(userpw.encode("latin-1"))
            generated_password_hash = m.hexdigest()
            new_user = Users(
                userid=userid, userpw=generated_password_hash, is_admin=False
            )
            user_otp = OTPStorages(
                userid=userid, otp_secret=pyotp.random_base32(), otp_verify=False
            )
            db.session.add(new_user)
            db.session.add(user_otp)
            db.session.commit()
            access_token = create_access_token(
                identity=new_user.userid, expires_delta=None
            )
            return jsonify({"result": True, "access_token": access_token})
        return jsonify({"result": False, "msg": "ID 또는 PW를 확인해주세요."})


@app.route("/signin", methods=["POST"])
def signin():
    if request.method == "POST":
        userid = request.json["userid"]
        userpw = request.json["userpw"]
        if userid and userpw:
            user = Users.query.filter_by(userid=userid).first()
            m = sha256()
            m.update(userpw.encode("latin-1"))
            generated_password_hash = m.hexdigest()
            if user and user.userpw == generated_password_hash:
                access_token = create_access_token(
                    identity=user.userid, expires_delta=None
                )
                return jsonify({"result": True, "access_token": access_token})
            else:
                return jsonify({"result": False, "msg": "ID/PW를 확인해주세요."})
    return jsonify({"result": False, "msg": "ID/PW를 확인해주세요."})


@app.route("/my_info", methods=["POST"])
@jwt_required()
def my_info():
    userid = get_jwt_identity()
    return jsonify({"result": True, "userid": userid})


@app.route("/otp_register", methods=["POST"])
@jwt_required()
def OTP_register():
    userid = get_jwt_identity()
    user = Users.query.filter_by(userid=userid).first()
    if user.is_admin:
        return jsonify({"result": False, "msg": "보안 정책상 관리자는 OTP 재등록이 불가능 합니다."})
    otp_storage = OTPStorages.query.filter_by(userid=userid).first()
    otp_url = pyotp.totp.TOTP(
        otp_storage.otp_secret, interval=OTP_VALID_SEC
    ).provisioning_uri(name=userid, issuer_name="SuperSecure OTP")

    return jsonify({"result": True, "otp_url": otp_url})


@app.route("/otp_view", methods=["POST"])
@jwt_required()
def OTP_view():
    if request.method == "POST":
        userid = get_jwt_identity()
        otp_storage = OTPStorages.query.filter_by(userid=userid).first()
        if otp_storage.otp_verify == False:
            return jsonify({"result": False, "msg": "OTP가 인증되지 않았습니다."})
        otp_storage.otp_verify = False
        db.session.commit()
        current_otp = pyotp.totp.TOTP(
            otp_storage.otp_secret, interval=OTP_VALID_SEC
        ).now()
        return jsonify({"result": True, "otp_code": current_otp})
    return jsonify({"result": False, "msg": "HTTP Method Error"})


@app.route("/otp_auth", methods=["POST"])
@jwt_required()
def OTP_auth():
    if request.method == "POST":
        otp_code = request.json["otp_code"]
        userid = get_jwt_identity()
        otp_storage = OTPStorages.query.filter_by(userid=userid).first()
        otp_result = pyotp.totp.TOTP(
            otp_storage.otp_secret, interval=OTP_VALID_SEC
        ).verify(otp_code, valid_window=OTP_VALID_SEC)
        if otp_result:
            otp_storage.otp_verify = True
            db.session.commit()
            return jsonify({"result": True, "msg": "Success"})
        else:
            return jsonify({"result": False, "msg": "Failed"})
    return jsonify({"result": False, "msg": "Failed"})


@app.route("/user_listing", methods=["POST"])
@jwt_required()
def user_listing():
    if request.method == "POST":
        userid = get_jwt_identity()
        otp_storage = OTPStorages.query.filter_by(userid=userid).first()
        if otp_storage.otp_verify == False:
            return jsonify({"result": False, "msg": "OTP가 인증되지 않았습니다."})
        otp_storage.otp_verify = False
        db.session.commit()
        user_list = list()
        users = Users.query.all()
        for user in users:
            user_dict = dict_stringfy(dict(user.__dict__))
            user_list.append(user_dict)
        return jsonify({"result": True, "users": user_list})
    return jsonify({"result": False, "msg": "HTTP Method Error"})


@app.route("/get_flag", methods=["POST"])
@jwt_required()
def get_flag():
    if request.method == "POST":
        userid = get_jwt_identity()
        otp_storage = OTPStorages.query.filter_by(userid=userid).first()
        if otp_storage.otp_verify == False:
            return jsonify({"result": False, "msg": "OTP가 인증되지 않았습니다."})
        otp_storage.otp_verify = False
        db.session.commit()
        user = Users.query.filter_by(userid=userid).first()
        if user.is_admin == False:
            return jsonify({"result": False, "msg": "관리자 권한이 아닙니다."})
        with open("flag.txt") as f:
            flag = f.read(32)
        return jsonify({"result": True, "msg": f"DH{{{flag}}}"})
    return jsonify({"result": False, "msg": "HTTP Method Error"})


def Setup():
    with app.app_context():
        if Users.query.filter_by(userid="admin").count() == 0:
            admin_user = Users(userid='admin', userpw='[DELETED]', is_admin=True) # sha256
            db.session.add(admin_user)
            db.session.commit()
        if OTPStorages.query.filter_by(userid="admin").count() == 0:
            admin_otp = OTPStorages(userid='admin', otp_secret='[DELETED]', otp_verify = False) # base32
            db.session.add(admin_otp)
            db.session.commit()


def dict_stringfy(dictionary):
    return dict(map(lambda x: (x[0], str(x[1])), dictionary.items()))


if __name__ == "__main__":
    Setup()
    app.run(host="0.0.0.0", port=8000)

import datetime
from sqlalchemy import Column, Integer, String, Boolean, DateTime
from flask_sqlalchemy import SQLAlchemy

db = SQLAlchemy()


def init_db(app):
    db.init_app(app)
    with app.app_context():
        db.create_all()
    return db


class Users(db.Model):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True)
    userid = Column(String(50), unique=True)
    userpw = Column(String(120))
    is_admin = Column(Boolean, default=False)
    join_time = Column(DateTime, default=datetime.datetime.utcnow)


class OTPStorages(db.Model):
    __tablename__ = "otp_storage"

    id = Column(Integer, primary_key=True)
    userid = Column(String(50), unique=True)
    otp_secret = Column(String(64))
    otp_verify = Column(Boolean, default=False)

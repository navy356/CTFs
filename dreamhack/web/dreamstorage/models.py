from sqlalchemy import Column, Integer, String, Boolean
from sqlalchemy_utils import UUIDType
import uuid
from flask_sqlalchemy import SQLAlchemy

db = SQLAlchemy()
def init_db(app):
    db.init_app(app)
    with app.app_context():
        db.create_all()
    return db

class Users(db.Model):
    __tablename__ = 'users'

    id = Column(Integer, primary_key=True)
    uid = Column(String(50), unique=True)
    upw = Column(String(120))
    user_uuid = Column(UUIDType(binary=False), default=uuid.uuid4)
    is_admin = Column(Boolean, default=False)

class Storages(db.Model):
    __tablename__ = 'storages'

    id = Column(Integer, primary_key=True)
    user_uuid = Column(UUIDType(binary=False), default=uuid.uuid4)
    filename = Column(String(50))
    data = Column(String())
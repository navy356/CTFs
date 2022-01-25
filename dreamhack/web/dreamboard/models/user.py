from sqlalchemy import Column, Integer, String, DateTime
from sqlalchemy.ext.declarative import declarative_base

Base = declarative_base()
class User (Base):
    __tablename__ = 'users'
    id = Column(Integer, primary_key=True)
    username = Column(String)
    password = Column(String)
    occupation = Column(String)
    level = Column(Integer)

    def __repr__(self):
        return "<User(username='%s', level='%d')>" % \
            (self.username, self.level)

    @classmethod
    def get_id(cls):
        return User.id

    @classmethod
    def find_by_username(cls, session, username):
        return session.query(User).filter(User.username == username).one()

    @classmethod
    def find_by_occupation(cls, session, occupation):
        return session.query(User).filter(User.occupation == occupation).limit(10).all()

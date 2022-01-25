from sqlalchemy import Column, Integer, String, DateTime, ForeignKey
from sqlalchemy import or_
from sqlalchemy.ext.declarative import declarative_base

from .user import User

Base = declarative_base()
class Post(Base):
    __tablename__ = 'posts'
    id = Column(Integer, primary_key=True)
    title = Column(String)
    content = Column(String)
    author = Column(Integer, ForeignKey(User.id))

    def __repr__(self):
        return "<Post(title='%s', author='%s')>" % \
            (self.title, self.author)

    @classmethod
    def get_id(cls):
        return Post.id

    @classmethod
    def find_by_author(cls, session, author_id):
        return session.query(Post).filter(or_(Post.author == author_id, Post.author == 1)).all()

    @classmethod
    def find_by_id(cls, session, post_id):
        return session.query(Post).filter(Post.id == post_id).one()

import datetime

from sqlalchemy import Column, Integer, String, DateTime, ForeignKey, Boolean
from sqlalchemy import or_, desc
from sqlalchemy.ext.declarative import declarative_base

from .user import User
from .post import Post

import time

Base = declarative_base()
class Report(Base):
    __tablename__ = 'reports'
    id = Column(Integer, primary_key=True)
    post = Column(String)
    author = Column(Integer, ForeignKey(User.id))
    checked = Column(Boolean, default=False)
    msg = Column(String, default='')
    report_time = Column(DateTime, default=datetime.datetime.utcnow)

    def __repr__(self):
        return "<Report(post='%s', author='%d', report_time='%s')>" % \
            (self.post, self.author, self.report_time)

    @classmethod
    def get_id(cls):
        return Post.id

    @classmethod
    def find_by_author(cls, session, author_id):
        return session.query(Report).order_by(Report.report_time.desc()).filter(or_(Report.author == author_id)).limit(1).one()

    @classmethod
    def get_reports_by_author_id(cls, session, author_id):
        return session.query(Report).order_by(Report.report_time.desc()).filter(or_(Report.author == author_id)).limit(10).all()


    @classmethod
    def get_reports(cls, session):
        x = session.query(Report).order_by(Report.report_time.asc()).filter(Report.checked == False).limit(10)
        return x.all()

    @classmethod
    def find_by_id(cls, session, report_id):
        return session.query(Report).filter(Report.id == report_id).one()

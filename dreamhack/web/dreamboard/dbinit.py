#!/usr/bin/env python3.8
from sqlalchemy import event
from sqlalchemy import create_engine
from sqlalchemy.orm import scoped_session
from sqlalchemy.orm import sessionmaker
from models import User, Post, Report
import random
import datetime
import argparse
import sys

from config import *

def _fk_pragma_on_connect(dbapi_con, con_record):
    dbapi_con.execute('PRAGMA foreign_keys = ON')

def init_db(uri):
    engine = create_engine(uri)
    event.listen(engine, 'connect', _fk_pragma_on_connect)
    User.metadata.create_all(engine)
    Post.metadata.create_all(engine)
    Report.metadata.create_all(engine)
    session_factory = sessionmaker(bind=engine)
    session = scoped_session(session_factory)

    return session

def build_db(db_uri):
    session = init_db(db_uri)
    # Admin User
    b = User(username=ADMIN_USERNAME, password=ADMIN_PASSWORD, occupation='admin', level=ADMIN_LEVEL)
    session.add(b)
    session.commit()

    user = User.find_by_username(session, ADMIN_USERNAME)
    p = Post(title='flag', content=FLAG, author=user.id)
    session.add(p)
    session.commit()

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Generate DB.')

    parser.add_argument('--out', dest='outfile',
                        help='output db file')

    args = parser.parse_args()

    if args.outfile is None:
        # parser.print_help()
        args.outfile = 'app.db'

    from os import system
    system(f'rm -rf app.db')

    build_db('sqlite:///' + args.outfile)

import requests
import logging
import datetime
import time
import json
import os
from aiohttp import web


MEASURE_FUNCS = os.getenv('BACKEND_MEASURE_FUNCS', False)
MEASURE_CHUNKS = os.getenv('BACKEND_MEASURE_CHUNKS', False)

# init logging
class RequestsHandler(logging.Handler):
    FLUSH_THRESHOLD = 50
    buf = []
    session = requests.Session()
    def emit(self, record):
        message = self.format(record)
        tag = record.name.replace('.', '/')
        data = { 'level': record.levelno, 'message': message,
            'time': int(time.time() * 1000) }
        self.buf.append(data)

        # Don't wait for important logs
        if record.levelno > logging.INFO or \
                len(self.buf) >= self.FLUSH_THRESHOLD:
            self.session.post(f'http://logger/backend/{tag}', json={
                'logs': self.buf
            })
            self.buf = []

logger = logging.getLogger(__name__)
logger.setLevel(logging.DEBUG)

ch = logging.StreamHandler()
ch.setLevel(logging.DEBUG)
ch.setFormatter(logging.Formatter('[%(levelname).1s] %(message)s'))

rh = RequestsHandler()
rh.setLevel(logging.INFO)

logger.addHandler(ch)
logger.addHandler(rh)

app = web.Application()

class measure:
    def __init__(self, name, log=print):
        self._logger = log
        self._name = name

    def __call__(self, func):
        def wrapper(*args, **kwargs):
            if not MEASURE_FUNCS:
                result = func(*args, **kwargs)
            else:
                start_time = datetime.datetime.now()
                result = func(*args, **kwargs)
                delta = datetime.datetime.now() - start_time
                duration = int(delta.total_seconds() * 1000)
                outdata = {
                    'measure': {
                        'name': self._name,
                        'duration_ms': duration,
                    }
                }
                self._logger(json.dumps(outdata))
            return result
        return wrapper

    def __enter__(self):
        if MEASURE_CHUNKS:
            self._start_time = datetime.datetime.now()

    def __exit__(self, type, value, traceback):
        if MEASURE_CHUNKS:
            delta = datetime.datetime.now() - self._start_time
            duration = int(delta.total_seconds() * 1000)
            outdata = {
                'measure': {
                    'name': self._name,
                    'duration_ms': duration,
                }
            }
            self._logger(json.dumps(outdata))

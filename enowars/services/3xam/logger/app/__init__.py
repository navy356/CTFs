import datetime
import json
import os


MEASURE_FUNCS = os.getenv('LOGGER_MEASURE_FUNCS', False)
MEASURE_CHUNKS = os.getenv('LOGGER_MEASURE_CHUNKS', False)

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

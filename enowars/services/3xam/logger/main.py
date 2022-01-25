from flask import Flask, request, abort
from app import logstore
from app import measure
import logging
import os
import time

app = Flask(__name__)

logstore_data_dir = os.getenv('LOGSTORE_DATA_DIR')
assert logstore_data_dir, 'Must set LOGSTORE_DATA_DIR env var'
assert os.path.isdir(logstore_data_dir), f'{logstore_data_dir} is not a directory'

max_service_size = 0
max_tag_size = 0
logstores = {}

@app.route('/', methods=('GET',), defaults={'service_name': ''})
@app.route('/<path:service_name>', methods=('GET',))
def get(service_name):
    with measure('Logger::get'):
        service_name_dissected = service_name.split('/')
        service_name = service_name_dissected[0]

        if not service_name:
            service_name = 'none'

        tag = '/' + ('/'.join(service_name_dissected[1:]))

        if service_name not in logstores:
           logstores[service_name] = logstore.LogStore(
                   os.path.join(logstore_data_dir, f'{service_name}.db'))

        svc_logstore = logstores[service_name]
        match = request.args.get('match') or None

        return {'logs': svc_logstore.get(service_name, tag, match=match)}

@app.route('/', methods=('POST',), defaults={'service_name': ''})
@app.route('/<path:service_name>', methods=('POST',))
def post(service_name):
    with measure('Logger::post'):
        global max_service_size, max_tag_size, log_stores

        service_name_dissected = service_name.split('/')
        service_name = service_name_dissected[0]

        if not service_name:
            service_name = 'none'

        tag = '/' + ('/'.join(service_name_dissected[1:]))

        if service_name not in logstores:
           logstores[service_name] = logstore.LogStore(
                   os.path.join(logstore_data_dir, f'{service_name}.db'))

        svc_logstore = logstores[service_name]

        json_data:dict = request.json
        if 'log' in json_data:
            logs = [json_data['log']]
        elif 'logs' in json_data:
            logs = json_data['logs']
        else:
            abort(400)

        # print(f'handling_batch of size: {len(logs)}:\n{json.dumps(logs, indent=4)}')

        records = []
        for log_data in logs:
            level = log_data['level'] if 'level' in log_data else logging.DEBUG
            timestamp = log_data['time'] if 'time' in log_data else int(time.time() * 1000)
            if 'message' not in log_data:
                abort(400)

            message = log_data['message']

            records.append((level, service_name, tag, message, timestamp))

        svc_logstore.save(records)
        return {'result': 'ok'}

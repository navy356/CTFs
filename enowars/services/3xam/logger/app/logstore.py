import sqlite3
from collections import OrderedDict
from . import measure

THRESHOLD_CLEANUP = 1000
CLEANUP_KEEP = 2000

class LogStore:
    def __init__(self, path):
        self._connection = sqlite3.connect(path)
        cur = self._connection.cursor()
        cur.execute('''CREATE TABLE IF NOT EXISTS
                        logs (_id INTEGER PRIMARY KEY AUTOINCREMENT,
                        level INTEGER, service TEXT, tag TEXT, message TEXT,
                        time INTEGER);''')
        cur.execute('''CREATE INDEX IF NOT EXISTS tag_index ON logs(tag);''')
        cur.execute('''CREATE INDEX IF NOT EXISTS message_index ON logs(message);''')
        self._connection.commit()
        self._inserts_count = THRESHOLD_CLEANUP - 1

    @measure('LogStore::save')
    def save(self, records):
        cur = self._connection.cursor()
        cur.executemany('''INSERT INTO logs (level, service, tag, message, time)
                VALUES (?, ?, ?, ?, ?)''', records)
        self._inserts_count += len(records)
        if self._inserts_count >= THRESHOLD_CLEANUP:
            cur.execute('''DELETE from logs
                           WHERE _id NOT IN
                           (
                             SELECT _id FROM logs
                             ORDER BY time DESC LIMIT ?
                           ); ''', (CLEANUP_KEEP,))
            self._inserts_count = 0
        self._connection.commit()

    @measure('LogStore::get')
    def get(self, service=None, tag=None, match=None):
        cur = self._connection.cursor()
        conds = OrderedDict()
        q = '''SELECT level, service, tag, message, time FROM logs'''

        if service:
            conds['service = ?'] = service
        if tag:
            conds['tag LIKE ?'] = f'{tag}%'
        if match:
            conds['message LIKE ?'] = f'%{match}%'

        q_conds = ' AND '.join(conds.keys())

        if q_conds:
            q = f'{q} WHERE {q_conds}'

        q = f'{q} ORDER BY _id DESC LIMIT 160'

        cur.execute(q, tuple(conds.values()))
        return [{
            'level': r[0],
            'service': r[1],
            'tag': r[2],
            'message': r[3],
            'time': r[4]
            } for r in cur.fetchall()][::-1]
            

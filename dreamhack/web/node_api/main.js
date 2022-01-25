const FLAG = function() {
    try {
        return require('fs').readFileSync('flag.txt').toString();
    } catch (err) {
        return 'DH{*****}';
    }
}()

const express = require('express');
const session = require('express-session');
const app = express();

const redis = require('redis');
const redis_client = redis.createClient();

const connectRedis = require('connect-redis');

const RedisStore = connectRedis(session);
const sess = {
    resave: false,
    secret: 'dreamhack',
    store: new RedisStore({
        client: redis_client
    }),
};

const db = {
    'guest': 'guest',
    'dreamhack': '1234',
    'ADMIN': 'this_is_admin?'
}

function login(user) {
    return user.userpw && db[user.userid] == user.userpw;
}

app.use(session(sess));
redis_client.set('log_info', 'KEY: "log_" + new Date().getTime(), VALUE: userid');

app.get('/show_logs', function(req, res) {
    // var log_query=get/log_info
    var log_query = req.query.log_query;
    try {
        log_query = log_query.split('/');
        if (log_query[0].toLowerCase() != 'get') {
            log_query[0] = 'get';
        }
        log_query[1] = log_query.slice(1)
    } catch (err) {
        // Todo
        // Error(403);
    }
    try {
        redis_client.send_command(log_query[0], log_query[1], function(err, result) {
            if (err) {
                res.send('ERR');
            } else {
                res.send(result);
            }
        })
    } catch (err) {
        res.send('try /show_logs?log_query=get/log_info')
    }
});

app.get('/login', function(req, res) {
    redis_client.set('log_' + new Date().getTime(), 'userid: ' + req.session.userid);
    if (login(req.query)) {
        req.session.userid = req.query.userid;
        res.send('<script>alert("login!");history.go(-1);</script>');
    } else {
        res.send('<script>alert("login failed!");history.go(-1);</script>');
    }
});

app.get('/flag', function(req, res) {
    if (req.session.userid === "admin") {
        res.send(FLAG)
    } else {
        res.send('hello ' + req.session.userid);
    }
});

app.get('/', function(req, res) {
    // Todo
    // res.render(...)
    res.send('hello ' + req.session.userid);
});

app.listen(8000, '0.0.0.0');

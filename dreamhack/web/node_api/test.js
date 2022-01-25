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

const db = {
    'guest': 'guest',
    'dreamhack': '1234',
    'ADMIN': 'this_is_admin?'
}

function login(user) {
    return user.userpw && db[user.userid] == user.userpw;
}


app.get('/show_logs', function(req, res) {
    // var log_query=get/log_info
    var log_query = req.query.log_query;
    console.log(log_query);
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
        console.log(log_query[0]);
        console.log(log_query[1]);
    } catch (err) {
        res.send('try /show_logs?log_query=get/log_info')
    }
});

app.get('/login', function(req, res) {
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
'use strict';
const express = require('express');
const cookieParser = require('cookie-parser');
const fs = require('fs');
const multer = require('multer');
const upload = require('./custom');
const mysql = require('mysql');
const jwt = require('jsonwebtoken');
const { spawnSync } = require('child_process');
const func = require('./userfunc');

const app = express();
const conn = func.connection();
const SECRET = process.env.SECRET;
const PORT = process.env.PORT;
const basedir = "./publics/uploads/"

console.log(`[*] The secret value is ${SECRET}`);

app.use(cookieParser());
app.use(express.urlencoded({ extended: true }));

app.set('views', __dirname + '/template');
app.set('view engine', 'ejs');

app.get('/', (req, res) => {
    res.render('index.ejs');
});

app.get('/login', (req, res) => {
    res.render('login.ejs');
});

app.post('/login', (req, res) => {
    const id = req.body.id;
    const pw = req.body.pw;

    if (id == '' || pw == '') {
        res.send("<script>alert('Empty values must not exist');history.go(-1);</script>");
    } else {
        func.getuser(mysql.format("SELECT * FROM users where id = ?", id), function(err, data) {
            if (err) {
                req.send(err);
            } else {
                if (data) {
                    console.log(func.sha256(pw))
                    if (func.sha256(pw) !== data.pw) { res.send("<script>alert('ID or password does not match.');history.go(-1);</script>"); } else {
                        const token = jwt.sign({ user: data.id }, SECRET, { expiresIn: '1h' });
                        res.cookie('user', token);
                        res.redirect("/");
                    }
                } else { res.send("<script>alert('User information does not exist.');history.go(-1);</script>") }
            }
        });
    }
});

app.get('/register', (req, res) => {
    res.render('register.ejs');
});

app.post('/register', (req, res) => {
    const name = req.body.name;
    const id = req.body.id;
    const pw = req.body.pw;
    const rpw = req.body.rpw;

    if (/[A-Z]/g.test(id) || id == 'korea_pocas') {
        res.send("This user is not allowed.").status(400);
    } else {
        if (name == '' || id == '' || pw == '') {
            res.send("<script>alert('Empty values must not exist');history.go(-1);</script>");
        } else {
            func.getuser(mysql.format("select * from users where id = ?", id), function(err, data) {
                if (err) {
                    res.send(err);
                } else {
                    if (data) {
                        res.send("<script>alert('This ID is already taken.');history.go(-1);</script>");
                    } else {
                        if (pw !== rpw) {
                            res.send("<script>alert('Please enter the same password');history.go(-1);</script>");
                        } else {
                            const params = [name.toLowerCase(), id.toLowerCase(), func.sha256(pw.toLowerCase())];
                            conn.query(mysql.format("insert into users(name, id, pw) values(?, ?, ?);", params), function(err, rows) {
                                if (err) { res.send(err); } else { res.redirect("/login"); }
                            });
                        }
                    }
                }
            });
        }
    }
});

app.get('/raw/:filename', function(req, res) {
    const file = {};
    const filename = req.params.filename;
    const filepath = `publics/uploads/${filename}`;

    try {
        func.getfile(mysql.format("select * from filelist where path = ?", filepath), function(err, data) {
            if (err) {
                res.send(err);
            } else {
                if (data) {
                    res.download(data.path);
                } else {
                    try {
                        func.merge(file, JSON.parse(`{"filename":"${filename}", "State":"Not Found"}`));
                        console.log(JSON.parse(`{"filename":"${filename}}"`));
                        res.send(file);
                    } catch (e) {
                        res.send("I don't know..");
                    }
                }
            }
        });
    } catch (e) {
        res.send("I don't know..");
    }
});

app.get('/upload', function(req, res) {
    res.render('upload.ejs');
});

app.post('/upload', upload.single('filezz'), function(req, res) {
    try {
        console.log(req.file.path);
        conn.query(mysql.format("insert into filelist(path) values (?)", req.file.path), function(err, rows) {
            if (err) { res.send(err); } else { res.send('Upload Success : ' + req.file.path); }
        });
    } catch (e) {
        res.send("I don't know..");
    }
})

app.get('/debug', function(req, res) {
    const cook = req.cookies['user'];
    if (cook !== undefined) {
        try {
            const information = jwt.verify(cook, SECRET);
            if (information['user'] == 'korea_pocas') {
                res.send(spawnSync(process.execPath, ['debug.js']).stdout.toString());
            } else {
                res.send("Debug mode off");
            }
        } catch (e) {
            res.status(401).json({ error: 'unauthorized' });
        }
    } else {
        try {
            res.send("You are not login..")
        } catch (e) {
            res.send("I don't know..")
        }
    }
})

app.get('/logout', (req, res) => {
    res.clearCookie("user");
    res.redirect("/");
});

app.listen(PORT, () => {
    console.log(`
                                                    Listeing PORT $ { PORT }....
                                                    `);
});
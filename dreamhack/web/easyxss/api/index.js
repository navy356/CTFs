const parse = require('url-parse');
const express = require('express');
const bot = require('./bot');
const router = express();

router.get('/', (req, res) => {
    const blog = req.query.blog || 'https://pocas.kr';
    const user = JSON.parse(`{"username":"Tester", "setblog":"${blog}"}`);
    const url = parse(user['setblog'], true)
    , hostname = url.hostname;

    if ((hostname === 'web-noob.kr' && user['username'] === 'hello') || (hostname === 'web-noob.kr' && username === 'world')) {
        console.log(1)
        res.render('index', {url:url});
    } else {
        res.render('index', {url:'#'});
    }
});

router.get('/flag', (req, res) => {
    const session = req.cookies.jsession || undefined;
    if (session === 'DELETE') {
        result = 'pocas{fakeflag}';
    } else {
        result = 'You are not admin';
    }
    res.render('flag', {result:result});
});

router.get('/report', (req, res) => {
    res.render('report.ejs');
});

router.post('/report', (req, res) => {
    try{
        bot(req.body.pay);
        res.send('<script>alert("Good Report!");history.go(-1);</script>');
    } catch (err) {
        res.send('<script>alert("Internal Server Error!");history.go(-1);</script>');
    }
});

module.exports = router;
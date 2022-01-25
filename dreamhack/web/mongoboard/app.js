var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var mongoose = require('mongoose');
var path = require('path');

// Connect to MongoDB
var db = mongoose.connection;
db.on('error', console.error);
db.once('open', function() {
    console.log("Connected to mongod server");
});
mongoose.connect('mongodb://localhost/mongoboard');

// model
var Board = require('./models/board');

// app Configure
app.use('/static', express.static(__dirname + '/public'));
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

app.all('/*', function(req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT");
    res.header("Access-Control-Allow-Headers", "Content-Type");
    next();
});

// router
var router = require(__dirname + '/routes')(app, Board);
app.get('/', function(req, res) {
    res.sendFile(path.join(__dirname + '/index.html'));
});

// run
var port = process.env.PORT || 8080;
var server = app.listen(port, function() {
    console.log("Express server has started on port " + port)
});
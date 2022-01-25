const express = require('express');
const morgan = require('morgan');
const cookieParser = require('cookie-parser');
const bodyParser = require('body-parser');
const crypto = require('crypto');
const sqlite3 = require('sqlite3').verbose();
const SqlString = require('sqlstring');

const {superSecretPassword, flag, salt} = require('./secret.js');

function createDb() {
    console.log("createDb chain");
    db = new sqlite3.Database('chain.sqlite3', createTable);
}


function createTable() {
    console.log("createTable secrets");
	db.run("DROP TABLE IF EXISTS secrets");
    db.run("CREATE TABLE IF NOT EXISTS secrets (user TEXT, token TEXT)", insertRows);
}

function insertRows() {
    console.log("insertRows Ipsum i");
    var stmt = db.prepare("INSERT INTO secrets VALUES (?,?)");

    stmt.run("admin", "48bb6e862e54f2a795ffc4e541caed4d");

    stmt.finalize(readAllRows);
}

function readAllRows() {
    console.log("readAllRows lorem");
    db.all("SELECT user AS user, token FROM secrets", function(err, rows) {
        rows.forEach(function (row) {
            console.log(row.user + ": " + row.token);
        });
    });
}

function closeDb() {
    console.log("closeDb");
    db.close();
}

function runChainExample() {
    createDb();
}

runChainExample();

const md5 = (s) => {
	let hex = crypto.createHash('md5');
	hex.update(s);
	return hex.digest('hex');
};

const sign = (s) => {
	return md5(`${salt}${s}`);
};

const getCookie = (obj) => {
	const json = {
		...obj,
		secret: sign(obj.user),
	};
	const s = JSON.stringify(json);

	return Buffer.from(s).toString('base64');
};

const fromCookie = (cookie) => {
	try {
		const s = Buffer.from(cookie, 'base64').toString();
		const obj = JSON.parse(s);
		console.log(obj);
		if (obj.secret !== sign(obj.user)) {
			return null;
		}
		return obj;
	} catch(e) {
		return null;
	}
};

const app = express();

app.use(morgan('combined'));
app.use(cookieParser());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.get('/', (req, res) => {
	res.send("Hack the planet");
});

app.post('/login', (req, res) => {
	const {user, pass, name} = req.body;

	if (user == null || name == null) {
		res.send("error");
		return;
	}

	/*if (user === 'admin' && pass !== superSecretPassword) {
		res.send("you're not the admin");
		return;
	}*/

	const cookie = getCookie({
		user,
		name: SqlString.escape(name),
	});
	
	const expires = new Date(Date.now() + 365 * 24 * 3600 * 1000);
	res.cookie('w1', cookie, {expires});
	res.send(`ok`);
});

app.get('/fetch', (req, res) => {
	const cookie = req.cookies.w1;
	if (cookie == null || cookie == "") {
		res.send("denied");
		return;
	}

	const session = fromCookie(cookie);

	if (session == null) {
		res.send("session void");
		return;
	}
	const name = req.query.name ? SqlString.escape(req.query.name) : session.name;
	console.log(`SELECT user, token FROM secrets WHERE user=${name}`);
	const result = db.prepare(`SELECT user, token FROM secrets WHERE user=${name}`).get();
	console.log(session.user);
	console.log(session);
	if (result == null) {
		res.send("not found");
		return;
	}
	console.log(result.user);
	if (result.user != session.user) {
		res.send("users dont match");
		return;
	}

	console.log(admin);
	if (result.user == 'admin' && md5(result.token) == '48bb6e862e54f2a795ffc4e541caed4d') {
		res.send(`you are clearly the admin: ${flag}`);
		return;
	}

	res.send("ok, bye");
});

app.listen(1337, () => {});
console.log('Listening');

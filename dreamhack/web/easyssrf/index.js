const http = require('http');
const path = require('path');
const urllib = require('url');
const express = require('express');

const FLAG = function() {
  return require('fs').readFileSync('./flag.txt').toString();
}();

const { validateURL, extractHostname } = require('./util.js');

const app = express();

app.set("views", __dirname + "/views");
app.set("view engine", "ejs");
app.engine("html", require("ejs").renderFile);  

app.use(express.urlencoded({ extended: true }));
app.use(express.static(path.join(__dirname, 'public')));

app.use(require('cookie-parser')());
app.use((req, res, next) => {
  if (req.socket.remoteAddress.replace(/^.*:/, '') === '127.0.0.1') {
    if (req.hostname === 'localhost') req.auth = 'admin';
    return next();
  }
  next();
});

app.get('/', (req, res) => {
  res.render('index', {
    response: null,
  });
});

app.get('/admin', (req, res) => {
  if (req.auth === 'admin') {
    res.render('admin', {
      flag: FLAG,
    });
  } else {
    res.status(400).end();
  }
});

app.post('/viewer', async (req, res) => {
  let response = '';
  let options;
  const { url } = req.body;
  if (!url) return res.status(400).end();
  try {
    const hostname = extractHostname(url);
    const { protocol, port, path, search } = urllib.parse(url);
    const [success, ip] = await validateURL(protocol, hostname);
    console.log(hostname);
    console.log(ip);
    console.log(success);
    if (!success) return res.status(400).send('Invalid URL.');
    if(port){ options = { protocol, host: ip, port: Number(port), path, search }; }
    else{ options = { protocol, host: ip, path, search }; }
    const data = new Promise((resolve, reject) => {
      const data = [];
        http.get(options, (res) => {
          res.on('data', (chunk) => {
            data.push(chunk);
          });
          res.on('end', () => resolve(Buffer.concat(data)));
        })
        .on('error', reject);
    });
    response = await data;
  } catch {
    return res.status(400).send('Url is down...');
  }
  res.render('index', {
    response: response,
  });
});

app.listen(80);
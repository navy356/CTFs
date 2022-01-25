const { v4: uuidv4 } = require('uuid');
uuidv4();
var db = new Map()

function createPaste(req, res) {
  var {content} = req.body;
  console.log("req.body = ");
  console.log(req.body);
  console.log("content = ");
  console.log(content);
  console.log(typeof content);
  noteid = uuidv4()
  db.set(noteid,{body:content})
  console.log((db.get(noteid)))
  return res.json(1);
  //res.redirect(301,`/paste.html?id=${noteid}`)
}

function getPaste(req, res) {
  return res.json(db.get(req.params.id))
}


module.exports = {getPaste,createPaste}

const mysql = require("mysql");
const crypto = require("crypto");

const dbconfig = require("./config/db.js");
const multer = require("multer");
const conn = dbconfig.init();

exports.connection = function() {
    return dbconfig.init();
}

exports.getfile = function (sql, callback){
    conn.query(sql, function (err, rows){
        if(err)
          callback(err, null);
        else
          callback(null, rows[0]);
    });
}

exports.getuser = function (sql, callback){
  conn.query(sql, function (err, rows){
      if(err)
        callback(err, null);
      else
        callback(null, rows[0]);
  });
}

exports.sha256 = function (data) {
    return crypto.createHash("sha256").update(data, "binary").digest("hex");
}

const isObject = function(obj) {
  return obj !== null && typeof obj === 'object';
}

const check = function(key){
  filter = ['outputFunctionName', 'path', 'file']
  for (let i = 0; i < filter.length; i++){
    if (filter[i] == key)
      return false
  }
  return true
}

exports.merge = function(a, b) {
  for (let key in b) {
    if(check(key)){
        if (isObject(a[key]) && isObject(b[key])) {
          this.merge(a[key], b[key]);
        } else {
          a[key] = b[key];
        }
    }
  }
  return a;
}
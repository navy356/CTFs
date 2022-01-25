var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var boardSchema = new Schema({
    title: {type:String, required: true},
    body: {type:String, required: true},
    author: {type:String, required: true},
    secret: {type:Boolean, default: false},
    publish_date: { type: Date, default: Date.now  }
}, {versionKey: false });

module.exports = mongoose.model('board', boardSchema);
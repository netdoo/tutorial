"use strict";

var log4js = require("log4js");

log4js.configure({
  "appenders": [
    { 
        "type": "console",
        "category" : "app"
    },
    {
      "type": "file",
      "filename": "log/log_file.log",
      "maxLogSize": 20480,
      "backups": 3,
      "category": "app"
    },
    {
      "type": "file",
      "filename": "log/log_file2.log",
      "maxLogSize": 1024,
      "backups": 3,
      "category": "app"
    }
  ]
});

var logger = log4js.getLogger('app');

var p1 = new Promise(function(resolve, reject) { 
    logger.info('p1 start');
    setTimeout(resolve, 1000, "one"); 
}); 

var p2 = new Promise(function(resolve, reject) { 
    logger.info('p2 start');
    setTimeout(resolve, 2000, "two"); 
});

var p3 = new Promise(function(resolve, reject) {
    logger.info('p3 start');
    setTimeout(resolve, 3000, "three");
});

var p4 = new Promise(function(resolve, reject) {
    logger.info('p4 start');
    setTimeout(function() {
        logger.info('p4 done');
        resolve("four");
    }, 3000);
});

Promise.all([p1, p2]).then(function(value) { 
    logger.info(value);
}, function(reason) {
    logger.error(reason)
});

var work = new Array();
work.push(p3,p4);

Promise.all(work).then(function(value) { 
    logger.info(value);
}, function(reason) {
    logger.error(reason)
});

/*
let RP = require("request-promise");  
let sites = await Promise.all([  
  RP("http://www.naver.com"),
  RP("http://www.google.com"),
  RP("http://www.yahoo.com")
]);
console.log(sites);
*/


var request = require('request-promise');
var html = request({
    url: 'https://raw.githubusercontent.com/JinHoKwon/nodejs-sample/master/README.md',
    method: 'GET'
    /*
    auth: {
        user: 'xxx',
        pass: 'yyy'
    },
    form: {
        'grant_type': 'client_credentials'
    }
    */
}, function(err, res) {
    logger.info(res.body);
});


logger.info('exit');

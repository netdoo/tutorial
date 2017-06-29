var express = require('express');
var session = require('express-session');
var redisstore = require('connect-redis')(session);
var redis = require('redis');
var cookieparser = require('cookie-parser');
var app = express();
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

app.use(log4js.connectLogger(logger, { level: log4js.levels.INFO }));
app.use(cookieparser('!@#%%@#@'));      // cookie 암호화. 암호화된 쿠키를 서버로 보냄.
app.use(session({
    secret: '1@%24^%$3^*&98&^%$', // 쿠키에 저장할 connect.sid값을 암호화할 키값 입력
    resave: false,                // 세션 아이디를 접속할때마다 새롭게 발급하지 않음
    saveUninitialized: true,      // 세션 아이디를 실제 사용하기전에는 발급하지 않음
    store:  new redisstore({host: 'localhost', port: 6379, client:redis.createClient(), resave:false})
}));

app.get('/cookie', function(req, res) {

    var count = parseInt(req.signedCookies.count);

    if (count) {
        count++;
        res.cookie('count', count, {signed:true, maxAge: 50000});
        res.send('cookie count ' + count);
    } else {
        count = 1;
        res.cookie('count', count, {signed:true, maxAge: 50000});
        res.send('cookie count is empty');    
    }

    logger.info('cookie count : %d', count);
});

app.get('/cookie_clear', function(req, res) {
    res.clearCookie('count');
    res.send('clear cookie');
});

app.get('/session', function (req, res) {

    if (req.session.count) {
        req.session.count++;
        res.send('session count ' + req.session.count);
    } else {
        req.session.count = 1;
        res.send('session count is empty');
    }
    
    logger.info('session count : %d', req.session.count);
});

app.get('/session_destory', function(req, res) {
    req.session.destroy();
    res.clearCookie('connect.sid');
    res.send('destory session');
});

app.listen(3000, function () {
    logger.info('Example app listening on port 3000!');
});

'use strict';

/**
 * express - log4j - mysql pool
 */
var fs = require('fs');
var _ = require('underscore');
var express = require('express');
var session = require('express-session');
var cookieparser = require('cookie-parser');
var app = express();
var flash = require('connect-flash');
var bodyParser = require('body-parser');
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

const HttpStatus = require('http-status-codes');
var mysql = require('promise-mysql');
var pool = mysql.createPool({
    connectionLimit : 10,
    host : '127.0.0.1',
    user : 'root',
    password : '',
    database : 'test'
});

var logger = log4js.getLogger('app');
var router = express.Router();

// middleware that is specific to this router
router.use(function timeLog(req, res, next) {
    console.log('Time: ', Date.now());
    next();
});

// Set port
let port = process.env.PORT || 3000;
let env = process.env.NODE_ENV || 'debug';

app.set('port', port);

app.use(log4js.connectLogger(logger, { level: log4js.levels.INFO }));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieparser('!@#%%@#@'));      // cookie 암호화. 암호화된 쿠키를 서버로 보냄.
app.use(session({
    secret: '1@%24^%$3^*&98&^%$', // 쿠키에 저장할 connect.sid값을 암호화할 키값 입력
    resave: false,                // 세션 아이디를 접속할때마다 새롭게 발급하지 않음
    saveUninitialized: true       // 세션 아이디를 실제 사용하기전에는 발급하지 않음
    //store:  new redisstore({host: 'localhost', port: 6379, client:redis.createClient(), resave:false})
}));


var passport = require('passport') //passport module add
var localStrategy = require('passport-local').Strategy;

app.use(flash());
app.use(passport.initialize());
app.use(passport.session());
app.use('/', express.static(__dirname + '/'));
app.use('/static', express.static(__dirname + '/static'));


app.get('/', function (req, res) {
    var name = req.query['name'];
    
    if (undefined == name) {
        name = 'guest';
        logger.info('name param is emtpy.');
    } else {
        logger.info('visit ' + name);
    }
    
    res.send('Hello ' + name);
});

function err_mysql_connection(err, connection, req, res) {
    if (connection != undefined) {
        pool.releaseConnection(connection);
    }
    
    res.json({"code" : 100, "status" : "Error in connection database"});
}


app.get('/flash', function(req, res) {
    req.flash('info', 'hello flash');
    res.redirect('/flash2');
});

app.get('/flash2', function(req, res) {
    var data = req.flash('info');
    res.send(data);
}) ;

passport.use('local-login', new localStrategy({
  usernameField: 'username',
  passwordField: 'password',
  passReqToCallback: true //인증을 수행하는 인증 함수로 HTTP request를 그대로  전달할지 여부를 결정한다
}, function (req, username, password, done) {

    var dbconn;
    pool.getConnection().then(function(connection) {
        dbconn = connection;
        return dbconn.query('SELECT * FROM tb_member WHERE name = ' + dbconn.escape(username));
    }).then(function(rows) {
        if (rows.length === 0) {
            logger.error('unknown userid : ' + username);
            return done(false, null, req.flash('login_error_msg', 'unknown user id'));
        }

        if (rows[0].pass === password) {
            return done(null, rows[0]);
        } 
        
        return done(false, null, req.flash('login_error_msg', 'invalid password'));
    }).then(function() {
        pool.releaseConnection(dbconn);
    }).catch(function(err) {

        if (err) {
            if (dbconn != undefined) {
                pool.releaseConnection(dbconn);
            }
            return done(false, null, req.flash('login_error_msg', err.message));
        } else {
            return done(false, null, req.flash('login_error_msg', 'unknown error'));
        }
    });
}));


passport.use('local-signup', new localStrategy({
  usernameField: 'username',
  passwordField: 'password',
  passReqToCallback: true //인증을 수행하는 인증 함수로 HTTP request를 그대로  전달할지 여부를 결정한다
}, function (req, username, password, done) {

    var dbconn;

    pool.getConnection().then(function (connection) {
        dbconn = connection;
        return dbconn.query('SELECT * FROM tb_member WHERE name = ' + dbconn.escape(username));
    }).then(function(rows) {
        if (rows.length === 0) {
            return dbconn.query('START TRANSACTION');
        } else {
            throw Error('Exist username');
        }
        
    }).then(function() {
        var data = [username, password];
        return dbconn.query('INSERT INTO tb_member (name, pass) VALUES (?, ?)', data);
    }).then(function() {
        return dbconn.query('COMMIT');
    }).then(function() {
        return dbconn.query('SELECT * FROM tb_member WHERE name = ? ', username);
    }).then(function(rows) {
        return done(null, rows[0]);
    }).then(function() {
        pool.releaseConnection(dbconn);
    }).catch(function(err) {

        if (err) {
            if (dbconn != undefined) {
                if (err.message === 'Exist username') {
                    pool.releaseConnection(dbconn);
                    return done(false, null, req.flash('signup_error_msg', err.message));
                } else {
                    dbconn.query('ROLLBACK', function() {
                        pool.releaseConnection(dbconn);
                        return done(false, null, req.flash('signup_error_msg', err.message));
                    });
                }                
            }
        } else {
            return done(false, null, req.flash('signup_error_msg', 'unknown error'));
        }
    });
}));

passport.serializeUser(function (user, done) {
    logger.info('serializeUser : ' + user);
    done(null, user)
});

passport.deserializeUser(function (user, done) {    
    logger.info('deserializeUser : ' + user);
    done(null, user);
});

app.post('/login', passport.authenticate('local-login', {failureRedirect: '/login_error', failureFlash: true}), function (req, res) {
    /// 인증이 성공한 경우.
    res.redirect('/home');
});

app.post('/signup', passport.authenticate('local-signup', {failureRedirect: '/signup_error', failureFlash:true}), function(req, res) {
    /// 회원가입이 성공한 경우
    res.redirect('/home');
});

app.get('/logout', function(req, res) {
    req.session.destroy();
    res.clearCookie('sid');
    res.clearCookie('connect.sid');
    res.send('logout');
});

/// 유저가 로그인 되어있는지 판단.
function isAuthenticated(req, res, next) {
    if (req.isAuthenticated()) {
        return next();
    }

    res.redirect('/static/login.html');
}

app.get('/home', isAuthenticated, function(req, res) {
    res.send(req.session.passport.user);
});

app.get('/login_error', function(req, res) {
    res.send(req.flash('login_error_msg'));
});

app.get('/signup_error', function(req, res) {
    res.send(req.flash('signup_error_msg'));
});

app.get('/select', function(req, res) {
    var dbconn;
    pool.getConnection().then(function(connection) {
        dbconn = connection;
        return dbconn.query('SELECT * FROM tb_member');
    }).then(function(rows) {
        return res.send(rows);
    }).then(function() {
        pool.releaseConnection(dbconn);
    }).catch(function(err) {

        if (err) {
            if (dbconn != undefined) {
                pool.releaseConnection(dbconn);
            }
            
            res.status(HttpStatus.INTERNAL_SERVER_ERROR).send({error: err, message: err.message});
        } else {
            res.status(HttpStatus.INTERNAL_SERVER_ERROR).send({error: 'unknown', message: 'unknown error'});
        }
    });
});

app.get('/insert', function(req, res) {
    var dbconn;
    var name = req.query['name'];

    pool.getConnection().then(function (connection) {
        dbconn = connection;
        return dbconn.query('START TRANSACTION');
    }).then(function() {
        return dbconn.query('INSERT INTO tb_member (name) VALUES (?)', 'bora');
    }).then(function() {
        return dbconn.query('INSERT INTO tb_member (name) VALUES (?)', name);
    }).then(function() {
        return dbconn.query('COMMIT');
    }).then(function() {
        pool.releaseConnection(dbconn);
        res.send('welcome : ' + name);
    }).catch(function(err) {

        if (err) {
            if (dbconn != undefined) {
                dbconn.query('ROLLBACK', function() {
                    pool.releaseConnection(dbconn);
                    return res.status(HttpStatus.INTERNAL_SERVER_ERROR).send({error: err, message: err.message});
                });
            }
        } else {
            res.status(HttpStatus.INTERNAL_SERVER_ERROR).send({error: 'unknown', message: 'unknown error'});
        }
    });
});


var server = app.listen(app.get('port'), function () {
    logger.info('Example app listening on port ' + app.get('port'));

    var dirs = ["log", "upload"];
    dirs.map((dir) => {
        if (!fs.existsSync(dir)) {
            console.log('mkdir : ' + dir);
            fs.mkdirSync(dir);
        } else {
            console.log('exist : ' + dir);
        }
    });

    /// db 접속에 실패한 경우, http server를 종료한다.
    pool.getConnection().then(function(connection) {
        logger.info('mysql connect success');
        pool.releaseConnection(connection);
    }).catch(function(err) {
        if (err) {
            logger.error('fail to connect mysql');
            server.close(function() {
                logger.info('close http server');
                process.exit(0);
            })
        }
    });
});

process.on('exit', function () {
    logger.info('exit app');
});

process.on('uncaughtException', function (err) {
    console.log('uncaughtException : ' + err);
})

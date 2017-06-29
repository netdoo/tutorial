'use strict';

/**
 * express - log4j - mysql pool
 */
var fs = require('fs');
var _ = require('underscore');
var express = require('express');
var app = express();
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
const fileUpload = require('express-fileupload');
var mysql = require('promise-mysql');
var pool = mysql.createPool({
    connectionLimit : 10,
    host : '127.0.0.1',
    user : 'root',
    password : '',
    database : 'test'
});

var logger = log4js.getLogger('app');

var crypto = require('crypto');


// Set router
var bird = require('./bird');

// Set port
let port = process.env.PORT || 3000;
let env = process.env.NODE_ENV || 'debug';

app.set('port', port);

app.use(log4js.connectLogger(logger, { level: log4js.levels.INFO }));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use('/bird', bird);
app.use('/', express.static(__dirname + '/'));
app.use('/static', express.static(__dirname + '/static'));
app.use(fileUpload({
  limits: { fileSize: 1 * 1024 * 1024 },
}));

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

app.get('/download/:filename', function(req, res) {
    var filename = req.params.filename;

    if (undefined == filename) {
        return res.status(HttpStatus.NOT_FOUND).send('Request file is not found');
    }

    var filepath = __dirname + "/" + filename;
    if (!fs.existsSync(filepath)) {
        return res.status(HttpStatus.NOT_FOUND).send('Request file is not found');
    }

    return res.download(filename, function(err) {
        if (err) {
            logger.error(err);
        } else {
            logger.info('sendfile : ' + filepath);
        }
    });
});

app.post('/single_upload', function(req, res) {
    if (!req.files)
        return res.status(400).send('No files were uploaded.');

    // The name of the input field (i.e. "sampleFile") is used to retrieve the uploaded file
    let sampleFile = req.files.sampleFile;
    var uploadpath = 'upload/' + req.files.sampleFile.name; 
    // Use the mv() method to place the file somewhere on your server
    sampleFile.mv(uploadpath, function(err) {
        
        if (err)
            return res.status(500).send(err);

        logger.info('upload : ' + uploadpath);
        res.send('File uploaded!');
    });
});

app.post('/multi_upload', function(req, res) {

    if (!req.files) 
        return res.status(400).send('No files were uploaded.');

    var upload_file_count = 0;
    var upload_err = new Array();
    var upload_promise_list = new Array();

    var promise = function(obj) {
        return new Promise(function(resolve, reject) {
            obj.mv('upload/' + obj.name, function(err) {
                if (err) {
                    reject(err);
                } else {
                    resolve(obj.name);
                }
            });
        });
    }

    var actions = _(req.files).toArray().map(promise);

    Promise.all(actions).then(function(data) {
        console.log('success : ' + data);
        res.send('multi_upload success');
    }, function(reason) {
        res.status(500).send(reason);
        console.log('error : ' + reason);
    }).catch(function(err) {
        console.log('exception : ' + err);
        res.status(500).send(err);
    });
});

app.get('/json', function(req, res) {
    res.json({"key1" : "value",
        "key2" : "value" 
    });
});

app.get('/json2', function(req, res) {
    res.json({key3 : "value",
        key4 : "value" 
    });
});

app.get('/crypto', function(req, res) {
    var password = req.query['password'];
    var encryptedPassword;

    if (undefined == password) {
        logger.info('password param is emtpy.');
        return res.status(HttpStatus.NOT_FOUND).send('password param is empty');
    }

    var sha1 = crypto.createHash('sha1');
    sha1.update(password);
    encryptedPassword = sha1.digest('hex');
    logger.info('encrypted password : ' + encryptedPassword);
    return res.send('Hello ' + encryptedPassword);
});

app.get('/process', function(req, res) {
    var exec = require('child-process-promise').exec;
    
    exec('cat c:/temp/readme2.txt')
    .then(function (result) {
        var stdout = result.stdout;
        var stderr = result.stderr;
        console.log('stdout: ', stdout);
        console.log('stderr: ', stderr);
        res.send(stdout);
    }).catch(function (err) {
        console.error('ERROR: ', err);
        res.send(err);
    });
});

app.get('/env', function(req, res) {
    res.send(env);
});

app.get('/sendmail', function(req, res) {

    /// https://www.google.com/settings/security/lesssecureapps
    /// 보안 수준이 낮은 앱 - 사용함 체크 필요.

    const nodemailer = require('nodemailer');
    var smtpTransport = require('nodemailer-smtp-transport');

    // create reusable transporter object using the default SMTP transport
    let transporter = nodemailer.createTransport(smtpTransport({
        service: 'gmail',
        auth: {
            user: 'id@gmail.com',
            pass: 'password'
        }
    }));

    // setup email data with unicode symbols
    let mailOptions = {
        from: '"권진호" <no-reply@gmail.com>', // sender address
        to: 'jhkwon78@tmon.co.kr', // list of receivers
        subject: 'Hello ✔', // Subject line
        text: 'Hello world ?', // plain text body
        html: '<b>Hello world ?</b>' // html body
    };

    // send mail with defined transport object
    transporter.sendMail(mailOptions, (error, info) => {
        if (error) {
            logger.error('fail to sendmail : ' + error);
            return res.send(error);
        }

        res.send(info);
        logger.info('sendmail : ' + info);
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

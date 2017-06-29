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
    } else {
        logger.info('sendmail response : %s' , info.response);
        transporter.close();
    }
});

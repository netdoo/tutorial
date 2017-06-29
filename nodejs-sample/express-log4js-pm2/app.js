var express = require('express');
var app = express();
var process = require('process');
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

app.get('/', function (req, res) {

    var name = req.query['name'];
    
    if (undefined == name) {
        name = 'guest';
        logger.info('name param is emtpy.');
    } else {
        logger.info('visit ' + name);
    }

    res.send('PID ' + process.pid + " , " + name);
});

app.listen(3000, function () {
    logger.info('Example app listening on port 3000!');
});


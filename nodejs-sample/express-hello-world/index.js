var express = require('express');
var app = express();
var port = 3000;

process.argv.forEach(function(val, index, array) {

   if (val.startsWith("port")) {
        var setting = val.split("=");
        port = setting[1];
        console.log('port ' + port);
        return true;
    }
});

app.get('/', function (req, res) {
  res.send('myport is ' + port);
});

app.listen(port, function () {
  console.log('Example app listening on port: ' + port);
});


# express lib 설정
```sh
$ mkdir express-hello-world
$ cd express-hello-world
$ npm init -f
$ npm i --save express
```

# 샘플 코드 구현
```javascript
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

```

# express 실행
```sh
$ node index.js port=3000
```

# expres 결과 확인
```sh 
$ curl localhost:3000
```


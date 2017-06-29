
# vi /etc/nginx/nginx.conf 
```
http {
    upstream my-app {
        least_conn;
        server 192.168.56.104:5000 weight=10 max_fails=2 fail_timeout=60s;
        server 192.168.56.104:5001 weight=10 max_fails=2 fail_timeout=60s;
        server 192.168.56.104:5002 weight=10 max_fails=2 fail_timeout=60s;
    }

    server {
        listen 8080;
        location / {
            proxy_pass http://my-app;
            proxy_http_version 1.1;
            proxy_set_header Upgrade $http_upgrade;
            proxy_set_header Connection 'upgrade';
            proxy_set_header Host $host;
            proxy_cache_bypass $http_upgrade;
        }
    } 
}
```


# 샘플 코드 작성 

```js
var express = require('express');
var app = express();
var id = uuid.v4();
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

# 샘플 서버 실행

```sh
$ node index.js port=5000
$ node index.js port=5001
$ node index.js port=5002
```

# nginx restart

```sh
$ sudo /etc/init.d/nginx restart
```

# 리버스 프록시 테스트 
```sh
$ curl localhost:8080
myport is 5000

$ curl localhost:8080
myport is 5001

$ curl localhost:8080
myport is 5002
```

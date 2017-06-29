
# docker-compose 설치

```sh
$ sudo apt-get install docker-compose
```


# 전체 디렉토리 구조
```sh
docker-compose.yml
myweb/
├─ Dockerfile
├─ index.js
├─ package.json 
nginx
├─ Dockerfile
└─ nginx.conf
```


# 더미 nodejs app 생성

```sh
$ npm init -f
$ npm i --save express
$ vi myweb/index.js
```
```js
var express = require('express');
var uuid = require('uuid');
var app = express();
var id = uuid.v4();
var port = 3000;

app.get('/', function (req, res) {
  res.send('uuid is ' + id);
});

app.listen(port, function () {
  console.log('Example app listening on port: ' + port);
});
```

# myweb/Dockerfile 작성 
```sh 
FROM node:6
COPY package.json /src/package.json
RUN  cd /src; npm install
COPY . /src
EXPOSE 3000
WORKDIR /src

CMD node index.js
```

# myweb Dockerfile 빌드

```sh
$ cd myweb
$ sudo docker build -t mynodejs .
```


# nginx.conf 파일 생성

```sh
$ vi nginx.conf
worker_processes 4;

events { worker_connections 1024; }

http {
    upstream node-app {
        least_conn;
        server app-1:3000 weight=10 max_fails=3 fail_timeout=30s;
        server app-2:3000 weight=10 max_fails=3 fail_timeout=30s;
        server app-3:3000 weight=10 max_fails=3 fail_timeout=30s;
    }

    server {
        listen 80;

        location / {
            proxy_pass http://node-app;
            proxy_http_version 1.1;
            proxy_set_header Upgrade $http_upgrade;
            proxy_set_header Connection 'upgrade';
            proxy_set_header Host $host;
            proxy_cache_bypass $http_upgrade;
        }
    }
}
```


# nginx/Dockerfile 생성

```sh
$ vi nginx/Dockerfile
FROM nginx
COPY nginx.conf /etc/nginx/nginx.conf
```


# nginx/Dockerfile 빌드 

```sh 
$ sudo docker build -t mynginx .

Sending build context to Docker daemon  3.584kB
Step 1/2 : FROM nginx
latest: Pulling from library/nginx
36a46ebd5019: Pull complete
57168433389f: Pull complete
332ec8285c50: Pull complete
Digest: sha256:c15f1fb8fd55c60c72f940a76da76a5fccce2fefa0dd9b17967b9e40b0355316
Status: Downloaded newer image for nginx:latest
 ---> 46102226f2fd
Step 2/2 : COPY nginx.conf /etc/nginx/nginx.conf
 ---> 748e5d4187b3
Removing intermediate container 7d78351853b4
Successfully built 748e5d4187b3
```


# nginx/docker-compose.yml 생성
```
version: '2'

services:
  nginx:
    container_name: mynginx
    image: mynginx
#   build: ./nginx
    links:
      - app-1:app-1
      - app-2:app-2
      - app-3:app-3
    ports:
      - 3000:80
    volumes:
      - /var/mynginx/data:/var/nginx/data
    depends_on:
      - app-1
      - app-2
      - app-3

  app-1:
    container_name: mynodejs-1
    image: mynodejs:latest
    volumes:
      - /var/mynodejs-1/data:/var/nodejs/data
    ports:
      - 3000

  app-2:
    container_name: mynodejs-2
    image: mynodejs:latest
    volumes:
      - /var/mynodejs-2/data:/var/nodejs/data
    ports:
      - 3000

  app-3:
    container_name: mynodejs-3
    image: mynodejs:latest
    volumes:
      - /var/mynodejs-3/data:/var/nodejs/data
    ports:
      - 3000
```

# docker-compose 실행 

```sh
$ sudo docker-compose up --build
Recreating mynodejs
Creating mynodejs-2
Creating mynodejs-1
Creating mynginx
Attaching to mynodejs-2, mynodejs-1, mynodejs-3, mynginx
mynodejs-2 | Example app listening on port: 3000
mynodejs-1 | Example app listening on port: 3000
mynodejs-3 | Example app listening on port: 3000
```


# 테스트
```sh
$ curl localhost:3000
uuid is ed960ad7-4402-46a1-b7d9-dc55da184e4c
$ curl localhost:3000
uuid is 29af75fc-74a6-4628-a6f5-221144b05bed
$ curl localhost:3000
uuid is 82503d35-f574-4b6b-b072-05e6bdcdfc8e
```


# docker-compose 중지
```sh 
$ sudo docker-compose down
```



# 실행중인 Docker로 attach 

```sh
$ sudo docker exec -i -t mynodejs-1 /bin/bash #by Name
$ sudo docker exec -i -t 492af45be20a /bin/bash #by ID
$ root@665b4a1e17b6:/#
```

# docker로 부터 빠져나오기

```sh
# exit
```

# docker attach 주의사항

```sh
$ docker attach <name> 
```
이렇게 attach한 경우에는 Ctrl + p + q로 중지해야 

실행중인 컨테이너의 종료를 막을 수 있음.



# ubuntu 컨테이너 실행 

> -it : 키보드 입력

> --rm : 프로세스가 종료되면 컨테이너가 자동 종료 


```sh 
$ sudo docker run --rm -it ubuntu:16.04 /bin/bash 
```

# ubuntu 컨테이너 종료
```sh 
# exit 
```


# redis 컨테이너 실행 

> -d : detached mode (백그라운드 모드)로 실행 

> -p : 컨테이너의 포트를 호스트의 포트로 연결 

```sh 
$ sudo  docker run -d -p 1234:6379 redis
```


# 컨테이너 목록 확인하기 
```sh 
$ sudo docker ps -a 
CONTAINER ID        IMAGE               COMMAND                  CREATED              STATUS              PORTS                    NAMES
fb9ae9b7238d        redis               "docker-entrypoint..."   About a minute ago   Up About a minute   0.0.0.0:1234->6379/tcp   modest_rosalind

```

# 컨테이너 중지 
```sh 
$ sudo docker stop <containerid>
$ sudo docker stop fb9ae9b7238d
fb9ae9b7238d
```

# 컨테이너 제거하기 
```sh 
$ sudo docker rm <containerid>
$ sudo docker rm fb9ae9b7238d
```

# 이미지 목록 
```sh 
$ sudo docker images
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
redis               latest              e32ef7250bc1        6 days ago          184MB
ubuntu              16.04               f7b3f317ec73        7 days ago          117MB
home@ubuntu:~$
```

# 이미지 삭제 
```sh 
$ sudo docker rmi <imageid>
$ sudo docker rmi e32ef7250bc1
Untagged: redis:latest
Untagged: redis@sha256:079a8abb332d42953d72040baeb7beae1585b2ca08502e154fc761d5b1a32ade
Deleted: sha256:e32ef7250bc17c4b27fbab655ad21f0819293b34dbbe5a8110f691e336dc6ac3
Deleted: sha256:9e1906dad873940d0cb23e118182e0242a20d1812cb131503dacf011cd1f340f
Deleted: sha256:0a1fc9f9cbf5d34ea08b2ea939d042062e7aac98cf8605d02e11f92bc744b742
Deleted: sha256:ffc9faf078b5c06a3f064cd7c8812331f2d49d260da580de3b58eecd3808e057
Deleted: sha256:8d1ca91667562747e3c71ba9b2842787fb1a1bb2e9afc257976d07ac0b3046a5
Deleted: sha256:cf45e507ee66372173f0f264446aab80826a1b6312620c8ec671ff032592d248
Deleted: sha256:ba6bd0a8da3391094b003c283d2e0cd45c0f5e3c219e07f931cd4a7d539cb7a6
Deleted: sha256:295d6a056bfd381abf9c462629066b0458373045a1dca7d13ef6128cb38c977e
```





# 도커와 디렉토리 공유 
```sh 
# 호스트 OS의 /dockerstorage 디렉토리를 /share 디렉토리명으로 공유함. 
$ sudo docker run -it -v /dockerstorage:/share myubuntu

# cd /share/
# echo "from docker" >> hello.dat
# exit
exit

$ cat /dockerstorage/hello.dat
from docker
```


# 모든 컨테이너 삭제 

```sh 
$ sudo docker rm `sudo docker ps -a -q`
998557f2d5f2
8b2450163eb5
cce42edab4fb
8979f43bd0e6
355a22d05d83
bceb7cb14f48
646e50fe5029
```


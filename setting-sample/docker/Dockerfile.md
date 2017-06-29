

# Dockerfile 


# 샘플 Dockerfile 작성 
```sh 
FROM ubuntu:16.04
MAINTAINER jhkwon78@tmon.co.kr
RUN touch data.txt
RUN echo "hello docker" >> data.txt
CMD ["/bin/bash"]
```

# 샘플 Dockerfile 빌드 

```sh 
$ sudo docker build -t myubuntu .
[sudo] password for home:
Sending build context to Docker daemon  2.048kB
Step 1/5 : FROM ubuntu:16.04
 ---> f7b3f317ec73
Step 2/5 : MAINTAINER jhkwon78@tmon.co.kr
 ---> Running in bb2ce4e08558
 ---> 114506c27107
Removing intermediate container bb2ce4e08558
Step 3/5 : RUN touch data.txt
 ---> Running in 2bbf4636cbe0
 ---> b133b23669eb
Removing intermediate container 2bbf4636cbe0
Step 4/5 : RUN echo "hello docker" >> data.txt
 ---> Running in d389740fc20e
 ---> 21232fe5fde0
Removing intermediate container d389740fc20e
Step 5/5 : CMD /bin/bash
 ---> Running in 2f002bdea193
 ---> 94dbd769fa0d
Removing intermediate container 2f002bdea193
Successfully built 94dbd769fa0d
```


# 생성된 docker 이미지 확인

```sh
$ sudo docker images
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
myubuntu            latest              94dbd769fa0d        31 seconds ago      117MB
ubuntu              16.04               f7b3f317ec73        7 days ago          117MB
```

# 생성된 docker 이미지 실행 
```sh 
$ sudo docker run -it myubuntu
```

# 컨테이너 안에서 Dockerfile에서 작성한 data.txt 파일이 있는지 확인 
```sh 
root@8979f43bd0e6:/# ll
total 76
drwxr-xr-x  34 root root 4096 May  2 03:01 ./
drwxr-xr-x  34 root root 4096 May  2 03:01 ../
-rwxr-xr-x   1 root root    0 May  2 03:01 .dockerenv*
drwxr-xr-x   2 root root 4096 Apr 17 20:31 bin/
drwxr-xr-x   2 root root 4096 Apr 12  2016 boot/
-rw-r--r--   1 root root   13 May  2 02:58 data.txt
drwxr-xr-x   5 root root  360 May  2 03:01 dev/
drwxr-xr-x  45 root root 4096 May  2 03:01 etc/
drwxr-xr-x   2 root root 4096 Apr 12  2016 home/
drwxr-xr-x   8 root root 4096 Sep 13  2015 lib/
drwxr-xr-x   2 root root 4096 Apr 17 20:30 lib64/
drwxr-xr-x   2 root root 4096 Apr 17 20:30 media/
drwxr-xr-x   2 root root 4096 Apr 17 20:30 mnt/
drwxr-xr-x   2 root root 4096 Apr 17 20:30 opt/
dr-xr-xr-x 192 root root    0 May  2 03:01 proc/
drwx------   2 root root 4096 Apr 17 20:31 root/
drwxr-xr-x   6 root root 4096 Apr 24 22:57 run/
drwxr-xr-x   2 root root 4096 Apr 24 22:57 sbin/
drwxr-xr-x   2 root root 4096 Apr 17 20:30 srv/
dr-xr-xr-x  13 root root    0 May  2 02:39 sys/
drwxrwxrwt   2 root root 4096 Apr 17 20:31 tmp/
drwxr-xr-x  11 root root 4096 Apr 24 22:57 usr/
drwxr-xr-x  13 root root 4096 Apr 24 22:57 var/

root@8979f43bd0e6:/# cat data.txt
hello docker
```


# 도커 설치

```sh
$ curl -s https://get.docker.com/ | sudo sh 
```

# 사용자 권한 부여

```sh
# someuser 사용자에게 권한주기
sudo usermod -aG docker someuser 
```

# 도커 설치 확인
```sh
$ sudo docker version
Client:
 Version:      17.04.0-ce
 API version:  1.28
 Go version:   go1.7.5
 Git commit:   4845c56
 Built:        Mon Apr  3 18:07:42 2017
 OS/Arch:      linux/amd64

Server:
 Version:      17.04.0-ce
 API version:  1.28 (minimum version 1.12)
 Go version:   go1.7.5
 Git commit:   4845c56
 Built:        Mon Apr  3 18:07:42 2017
 OS/Arch:      linux/amd64
 Experimental: false
```

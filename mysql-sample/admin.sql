
mysql.exe -u root -h localhost

mysql> UPDATE mysql.user SET password=password("newpassword") WHERE user="root";
mysql> Flush Privileges;
 
mysql> CREATE USER 'monty'@'localhost' IDENTIFIED BY 'some_pass';
mysql> GRANT ALL PRIVILEGES ON *.* TO 'monty'@'localhost' WITH GRANT OPTION;
 
mysql> CREATE USER 'monty'@'%' IDENTIFIED BY 'some_pass';
mysql> GRANT ALL PRIVILEGES ON *.* TO 'monty'@'%' WITH GRANT OPTION;
 
 
 
################################################################## 
# 모든 IP 허용
##################################################################

INSERT INTO mysql.user (host,user,password) VALUES ('%','root',password('패스워드'));
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%';
FLUSH PRIVILEGES;
 
 
 
##################################################################
IP 대역 허용
다음과 같이 설정하면 111.222로 시작하는 모든 IP가 허용된다.
##################################################################
 
INSERT INTO mysql.user (host,user,password) VALUES ('111.222.%','root',password('패스워드'));
GRANT ALL PRIVILEGES ON *.* TO 'root'@'111.222.%';
FLUSH PRIVILEGES;
 
 
 
################################################################## 
특정 IP 1개 허용
##################################################################

INSERT INTO mysql.user (host,user,password) VALUES ('111.222.33.44','root',password('패스워드'));
GRANT ALL PRIVILEGES ON *.* TO 'root'@'111.222.33.44';
FLUSH PRIVILEGES;

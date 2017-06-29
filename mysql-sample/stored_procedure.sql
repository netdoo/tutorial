

DROP TABLE IF EXISTS `users`; 

CREATE TABLE `users` ( 
`user_no` INTEGER AUTO_INCREMENT, 
`user_id` varchar(10) NOT NULL, 
`user_name` varchar(10) NULL DEFAULT NULL, 
`user_email` varchar(20) NULL DEFAULT NULL, 
`user_type` ENUM('SA', 'ADMIN', 'MANAGER', 'DBA', 'GUEST') NOT NULL DEFAULT 'GUEST', 
PRIMARY KEY (`user_no`) ) ENGINE=InnoDB DEFAULT CHARSET=utf8; 


DROP PROCEDURE IF EXISTS insert_many_records;

##############################################################
# 더미 데이터 입력용 프로시저 생성
##############################################################

DELIMITER $$
CREATE PROCEDURE insert_many_records(_vEnd INT)
BEGIN
    DECLARE vStart INT DEFAULT 0;
     
    WHILE vStart < _vEnd DO
        SET vStart = vStart + 1;
        INSERT INTO users (user_id, user_name, user_email) VALUES (CONCAT('id', vStart), CONCAT('name', vStart), CONCAT('mail', vStart));
         
    END WHILE;
END;$$

##############################################################
# 더미 데이터 입력
##############################################################
CALL insert_many_records(100);

SELECT *
FROM users;
















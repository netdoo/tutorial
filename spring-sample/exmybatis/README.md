```sql
DROP TABLE `users`;

CREATE TABLE `users` (
	`user_no` INT(11) NOT NULL AUTO_INCREMENT,
	`user_id` VARCHAR(50) NOT NULL,
	`user_name` VARCHAR(50) NULL DEFAULT NULL,
	`user_email` VARCHAR(50) NULL DEFAULT NULL,
	`user_type` ENUM('SA','ADMIN','MANAGER','DBA','GUEST') NOT NULL DEFAULT 'GUEST',
	PRIMARY KEY (`user_no`),
	INDEX `idx_user_id` (`user_id`)
)
COLLATE='utf8_general_ci'
ENGINE=InnoDB
;

INSERT INTO users (user_id, user_name, user_email) VALUES ('james', '제임스', 'james@gmail.com');
INSERT INTO users (user_id, user_name, user_email, user_type) VALUES ('sa', '수퍼 관리자', 'sa@gmail.com', 'SA');
INSERT INTO users (user_id, user_name, user_email, user_type) VALUES ('root', '루트 관리자', 'root@gmail.com', 'ADMIN');
INSERT INTO users (user_id, user_name, user_email, user_type) VALUES ('sys', '시스템 관리자', 'sys@gmail.com', 'ADMIN');
INSERT INTO users (user_id, user_name, user_email, user_type) VALUES ('dba', 'DB 관리자', 'dba@gmail.com', 'ADMIN');

SELECT *
FROM users
LIMIT 2, 3;

```

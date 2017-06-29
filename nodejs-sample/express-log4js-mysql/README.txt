예제를 테스트 하려면, mysql 안에 test 데이터베이스가 있어야 하고
tb_member 테이블이 다음과 같이 생성되어 있어야 합니다.

<pre><code>
DROP TABLE tb_member;

CREATE TABLE `tb_member` (
	`member_no` INT(11) NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(100) NOT NULL COMMENT '이름',
	`level` ENUM('VIP','GOLD','FAMILY','GUEST') NOT NULL DEFAULT 'GUEST' COMMENT '맴버레밸',
	`is_active` ENUM('Y','N') NOT NULL DEFAULT 'Y',
	`create_datetime` DATETIME NOT NULL DEFAULT NOW(),
	`update_datetime` DATETIME NOT NULL DEFAULT NOW(),
	PRIMARY KEY (`member_no`)
)
COMMENT='맴버정보 테이블'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1;

INSERT INTO tb_member(name) VALUES ('Jane');

SELECT * FROM tb_member;
</code></pre>

  

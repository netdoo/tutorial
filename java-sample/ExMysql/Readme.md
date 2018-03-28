sample doc remote

# mysql 설정

```concept
[mysqld]
wait_timeout = 30
interactive_timeout = 30
log_timestamps=SYSTEM

# General and Slow logging.
log-output=FILE
general-log=1
general_log_file="C:/logs/mysql/mysql.log"
slow-query-log=1
slow_query_log_file="C:/logs/mysql/mysql_slow.log"
long_query_time=10
```

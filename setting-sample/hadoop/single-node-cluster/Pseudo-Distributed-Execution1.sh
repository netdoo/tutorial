-- 파일시스템 포멧
# bin/hdfs namenode -format

-- 네임노드, 데이터노드 데몬 실행
# sbin/start-dfs.sh

-- 네임노드 접속
웹 브라우져에서 http://localhost:9870로 접속

-- 맵리듀스 작업에 필요한 HDFS 디렉토리 생성
# bin/hdfs dfs -mkdir /user 
# bin/hdfs dfs -mkdir /user/ 

-- 예제 파일 을 분산 파일시스템에 복사
# bin/hdfs dfs -mkdir input 
# bin/hdfs dfs -put etc/hadoop/*.xml input 

-- 예제 실행
# bin/hadoop jar share/hadoop/mapreduce/hadoop-mapreduce-examples-3.0.0-alpha1.jar grep input output 'dfs[a-z.]+' 

-- 분산파일시스템 결과를 로컬파일시스템으로 복사
# bin/hdfs dfs -get output output 
# cat output/* 
# bin/hdfs dfs -cat output/* 

-- 데몬 종료
# sbin/stop-dfs.sh

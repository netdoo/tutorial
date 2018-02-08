

# 명령줄에서 배치작업 실행

```sh
$ java -cp "target/dependency-jars/*;target/ExChunkBatch.jar" org.springframework.batch.core.launch.support.CommandLineJobRunner batch/job.xml myBatchJob
$ java -cp "target/dependency-jars/*;target/ExChunkBatch.jar" org.springframework.batch.core.launch.support.CommandLineJobRunner batch/job2.xml anotherJob
```

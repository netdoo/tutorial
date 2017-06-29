# mkdir input
# cp etc/hadoop/*.xml input
# bin/hadoop jar share/hadoop/mapreduce/hadoop-mapreduce-examples-3.0.0-alpha1.jar grep input output 'dfs[a-z.]+'

2017-05-20 14:48:05,634 INFO impl.MetricsConfig: loaded properties from hadoop-metrics2.properties
2017-05-20 14:48:06,108 INFO impl.MetricsSystemImpl: Scheduled snapshot period at 10 second(s).
2017-05-20 14:48:06,109 INFO impl.MetricsSystemImpl: JobTracker metrics system started
2017-05-20 14:48:07,057 INFO input.FileInputFormat: Total input files to process : 8
2017-05-20 14:48:07,175 INFO mapreduce.JobSubmitter: number of splits:8
2017-05-20 14:48:07,754 INFO mapreduce.JobSubmitter: Submitting tokens for job: job_local744764458_0001
2017-05-20 14:48:09,027 INFO mapreduce.Job: The url to track the job: http://localhost:8080/
2017-05-20 14:48:09,037 INFO mapreduce.Job: Running job: job_local744764458_0001
2017-05-20 14:48:09,045 INFO mapred.LocalJobRunner: OutputCommitter set in config null
2017-05-20 14:48:09,081 INFO output.FileOutputCommitter: File Output Committer Algorithm version is 2
2017-05-20 14:48:09,082 INFO output.FileOutputCommitter: FileOutputCommitter skip cleanup _temporary folders under output directory:false, ignore cleanup failures: false
2017-05-20 14:48:09,089 INFO mapred.LocalJobRunner: OutputCommitter is org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter
2017-05-20 14:48:09,227 INFO mapred.LocalJobRunner: Waiting for map tasks
2017-05-20 14:48:09,229 INFO mapred.LocalJobRunner: Starting task: attempt_local744764458_0001_m_000000_0
2017-05-20 14:48:09,364 INFO output.FileOutputCommitter: File Output Committer Algorithm version is 2
2017-05-20 14:48:09,364 INFO output.FileOutputCommitter: FileOutputCommitter skip cleanup _temporary folders under output directory:false, ignore cleanup failures: false
2017-05-20 14:48:09,454 INFO mapred.Task:  Using ResourceCalculatorProcessTree : [ ]
2017-05-20 14:48:09,492 INFO mapred.MapTask: Processing split: file:/root/hadoop-3.0.0-alpha1/input/hadoop-policy.xml:0+9683
2017-05-20 14:48:09,830 INFO mapred.MapTask: (EQUATOR) 0 kvi 26214396(104857584)
2017-05-20 14:48:09,835 INFO mapred.MapTask: mapreduce.task.io.sort.mb: 100
2017-05-20 14:48:09,836 INFO mapred.MapTask: soft limit at 83886080
2017-05-20 14:48:17,493 INFO mapred.Task: Task 'attempt_local765136099_0002_r_000000_0' done.
2017-05-20 14:48:17,494 INFO mapred.LocalJobRunner: Finishing task: attempt_local765136099_0002_r_000000_0
2017-05-20 14:48:17,494 INFO mapred.LocalJobRunner: reduce task executor complete.
2017-05-20 14:48:17,712 INFO mapreduce.Job: Job job_local765136099_0002 running in uber mode : false
2017-05-20 14:48:17,713 INFO mapreduce.Job:  map 100% reduce 100%
2017-05-20 14:48:17,714 INFO mapreduce.Job: Job job_local765136099_0002 completed successfully
2017-05-20 14:48:17,745 INFO mapreduce.Job: Counters: 30
        File System Counters
                FILE: Number of bytes read=1274818
                FILE: Number of bytes written=2729870
                FILE: Number of read operations=0
                FILE: Number of large read operations=0
                FILE: Number of write operations=0
        Map-Reduce Framework
                Map input records=1
                Map output records=1
                Map output bytes=17
                Map output materialized bytes=25
                Input split bytes=129
                Combine input records=0
                Combine output records=0
                Reduce input groups=1
                Reduce shuffle bytes=25
                Reduce input records=1
                Reduce output records=1
                Spilled Records=2
                Shuffled Maps =1
                Failed Shuffles=0
                Merged Map outputs=1
                GC time elapsed (ms)=86
                Total committed heap usage (bytes)=274194432
        Shuffle Errors
                BAD_ID=0
                CONNECTION=0
                IO_ERROR=0
                WRONG_LENGTH=0
                WRONG_MAP=0
                WRONG_REDUCE=0
        File Input Format Counters
                Bytes Read=123
        File Output Format Counters
                Bytes Written=23
root@mynode1:~/hadoop-3.0.0-alpha1#

# cat output/*
1       dfsadmin

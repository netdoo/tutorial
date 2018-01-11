

# Kafka 로그삭제 설정

3분 까지의 로그만 보관하고, 그외는 삭제하는 설정임. 

## server.properties

```
log.retention.minutes=3

# A size-based retention policy for logs. Segments are pruned from the log unless the remaining
# segments drop below log.retention.bytes. Functions independently of log.retention.hours.
log.retention.bytes=1073741824

# The maximum size of a log segment file. When this size is reached a new log segment will be created.
log.segment.bytes=1073741824
log.roll.ms=60000

# The interval at which log segments are checked to see if they can be deleted according
# to the retention policies
log.retention.check.interval.ms=3000
log.cleanup.policy=delete
```


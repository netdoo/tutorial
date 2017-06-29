# WordCountApp

```sh
$ bin\windows\zookeeper-server-start.bat config\zookeeper.properties
$ bin\windows\kafka-server-start.bat config\server.properties
$ bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic TextLinesTopic
$ bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic WordsWithCountsTopic
$ bin\windows\kafka-topics.bat --list --zookeeper localhost:2181
$ bin\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic TextLinesTopic
```



# MapApp

```sh
$ bin\windows\zookeeper-server-start.bat config\zookeeper.properties
$ bin\windows\kafka-server-start.bat config\server.properties
$ bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic MapAppTopic
$ bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic UpperMapAppTopic
$ bin\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic MapAppTopic
```





```sh
# spark-shell
Spark context Web UI available at http://192.168.56.104:4040
Spark context available as 'sc' (master = local[*], app id = local-1496979744052).
Spark session available as 'spark'.
Welcome to
      ____              __
     / __/__  ___ _____/ /__
    _\ \/ _ \/ _ `/ __/  '_/
   /___/ .__/\_,_/_/ /_/\_\   version 2.1.1
      /_/

Using Scala version 2.11.8 (OpenJDK 64-Bit Server VM, Java 1.8.0_121)
```

# 샘플 데이터 입력

```sh
$ curl -XPOST 'localhost:9200/bank/account/_bulk?pretty' --data-binary "@accounts.json"
$ curl -XGET 'http://localhost:9200/_count?pretty'
```

# Elasticsearch for Apache Hadoop 5.1.2 설치

```sh
$ curl -O http://download.elastic.co/hadoop/elasticsearch-hadoop-5.1.2.zip
```

# Elasticsearch Spark 연동 (Scala Version)
```sh
$ spark-shell --jars /root/temp/elasticsearch-hadoop-5.1.2/dist/elasticsearch-spark-20_2.11-5.1.2.jar


import org.apache.spark.sql.SQLContext
import org.elasticsearch.spark.sql._
import org.apache.spark.SparkConf

sc.stop() // 기존 SparkContext 종료

val conf = new SparkConf().setAppName("jsheo-test")

conf.set("spark.driver.allowMultipleContexts", "true")
conf.set("es.index.auto.create", "true")
conf.set("es.nodes.discovery", "true")
conf.set("es.nodes", "127.0.0.1:9200") // 각자 사용하는 ES 주소를 적는다.

// SparkContext랑 SQLConext 새로 만들기
val sc = new org.apache.spark.SparkContext(conf);

val sqlContext = new SQLContext(sc)

// 앞선 Sample Data에서 bank/account에 자료를 입력했다.
// bank는 ES Index를 의미하고
// account는 Type을 의미한다.
val df = sqlContext.read.format("org.elasticsearch.spark.sql").load("bank/account")
df.registerTempTable("tab")
sqlContext.sql("SELECT COUNT(*) FROM tab").show()
sqlContext.sql("SELECT firstname, lastname, email FROM tab").show()
sqlContext.sql("SELECT firstname, lastname, email FROM tab").show(5)
sqlContext.sql("SELECT firstname, lastname, email FROM tab").show(5, false)
sqlContext.sql("SELECT COUNT(DISTINCT city) FROM tab").show()
sqlContext.sql("SELECT COUNT(*) FROM (SELECT city FROM tab GROUP BY city) t").show()
sqlContext.sql("SELECT firstname, lastname, email FROM tab").write.csv("file:///root/temp/output")
```

# Elasticsearch Spark 연동 (Java Version)

```sh
$ spark-submit --class com.exsparkelastic.App /root/temp/ExSparkElastic-jar-with-dependencies.jar
```


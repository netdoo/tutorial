# Scala 설치
```sh
$ wget http://www.scala-lang.org/files/archive/scala-2.11.8.tgz
$ sudo mkdir /usr/local/src/scala
$ sudo tar xvf scala-2.11.8.tgz -C /usr/local/src/scala/
```

# Scala 환경변수 
```sh
$ vim ~/.bashrc
export SCALA_HOME=/usr/local/src/scala/scala-2.11.8
export PATH=$SCALA_HOME/bin:$PATH
```

## Scala 환경변수 로드
```sh
$ source ~/.bashrc
```

## Scala 버전 확인
```sh
$ scala -version

Scala code runner version 2.11.8 -- Copyright 2002-2016, LAMP/EPFL
```

# Apache Spark 설치
```
$ wget https://d3kbcqa49mib13.cloudfront.net/spark-2.1.1-bin-hadoop2.7.tgz
$ tar xvfz spark-2.1.1-bin-hadoop2.7.tgz
$ cd spark-2.1.1-bin-hadoop2.7/
```


# Apache Spark 로그 설정
```sh
$ cd spark-2.1.1-bin-hadoop2.7/
$ cp conf/log4j.properties.template conf/log4j.properties
$ vim conf/log4j.properties

# Set everything to be logged to the console
log4j.rootCategory=WARN, console
```

# Apache Spark 버전 확인
```sh
$ bin/spark-shell
Spark context Web UI available at http://192.168.56.103:4040
Spark context available as 'sc' (master = local[*], app id = local-1495678228537).
Spark session available as 'spark'.
Welcome to
      ____              __
     / __/__  ___ _____/ /__
    _\ \/ _ \/ _ `/ __/  '_/
   /___/ .__/\_,_/_/ /_/\_\   version 2.1.1
      /_/

Using Scala version 2.11.8 (OpenJDK 64-Bit Server VM, Java 1.8.0_121)
Type in expressions to have them evaluated.
Type :help for more information.

scala> sc.version
res0: String = 2.1.1

scala> :quit
```








# WorCount 예제 작성
pom.xml
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.exsparkbasic</groupId>
  <artifactId>ExSparkBasic</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>jar</packaging>

  <name>ExSparkBasic</name>
  <url>http://maven.apache.org</url>

  <properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>

  <dependencies>
    <dependency>
      <groupId>org.apache.spark</groupId>
      <artifactId>spark-core_2.10</artifactId>
      <version>2.1.0</version>
    </dependency>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.slf4j</groupId>
      <artifactId>slf4j-log4j12</artifactId>
      <version>1.8.0-alpha1</version>
    </dependency>
  </dependencies>

  <build>
    <finalName>ExSparkBasic</finalName>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
          <source>1.8</source>
          <target>1.8</target>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>
```
WordCount.java
```java
package com.exsparkbasic;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.*;
import org.apache.spark.api.java.function.*;

import scala.Tuple2;

public class WordCount implements Serializable {
    public void execute(String inputPath, String outputFile) {

        SparkConf conf = new SparkConf().setMaster("local").setAppName("WordCount");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaRDD<String> input = sc.textFile(inputPath);
        JavaRDD<String> words = input.flatMap(new FlatMapFunction<String, String>() {

            /// https://stackoverflow.com/questions/38880956/spark-2-0-0-arrays-aslist-not-working-incompatible-types/38881118#38881118
            /// In 2.0, FlatMapFunction.call() returns an Iterator rather than Iterable. Try this:
            public Iterator<String> call(String x) {
                return Arrays.asList(x.split(" ")).iterator();
            }
        });
        JavaPairRDD<String, Integer> counts = words.mapToPair(new PairFunction<String, String, Integer>() {
            public Tuple2<String, Integer> call(String x) {
                return new Tuple2(x, 1);
            }
        }).reduceByKey(new Function2<Integer, Integer, Integer>() {
            public Integer call(Integer x, Integer y) throws Exception {
                return x + y;
            }
        });

        counts.saveAsTextFile(outputFile);
        sc.stop();
    }

    public static void main( String[] args ) {
        String inputFile = "/root/README.md";       /// args[0];
        String outputFile = "/root/wordCount/";     /// args[1];
        WordCount wc = new WordCount();
        wc.execute(inputFile, outputFile);
    }
}
```


# WordCount 예제 실행
```
$ bin/spark-submit --class com.exsparkbasic.WordCount /root/ExSparkBasic.jar
$ cat /root/wordCount/*


(get,1)
(,71)
(information,1)
(core,1)
(web,1)
("local[N]",1)
(programs,2)
(option,1)
(MLlib,1)
(["Building,1)
(contributing,1)
(shell:,2)
(instance:,1)
(Scala,,1)
(and,9)
(command,,2)
(package.),1)
(./dev/run-tests,1)
(sample,1)
$
```

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

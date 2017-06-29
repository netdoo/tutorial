package com.exsparkelastic;

import java.io.Serializable;
import org.apache.spark.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App implements Serializable {
    final static Logger logger = LoggerFactory.getLogger(App.class);

    public App() {
    }

    public void execute() throws Exception{
        SparkSession sparkSession = SparkSession.builder()
                .appName("WithElastic")
                .master("local[*]")
                .config("spark.driver.allowMultipleContexts", "true")
                .config("es.index.auto.create", "true")
                .config("es.nodes.discovery", "true")
                .config("es.nodes", "127.0.0.1:9200")
                .getOrCreate();

        SQLContext sqlContext = new SQLContext(sparkSession);
        Dataset<Row> dataset = sqlContext.read().format("org.elasticsearch.spark.sql").load("bank/account");

        dataset.createTempView("tab");

        sqlContext.sql("SELECT COUNT(*) FROM tab").show();
        sqlContext.sql("SELECT COUNT(*) FROM tab").show();
        sqlContext.sql("SELECT firstname, lastname, email FROM tab").show();
        sqlContext.sql("SELECT firstname, lastname, email FROM tab").show(5);
        sqlContext.sql("SELECT firstname, lastname, email FROM tab").show(5, false);
        sqlContext.sql("SELECT COUNT(DISTINCT city) FROM tab").show();
        sqlContext.sql("SELECT COUNT(*) FROM (SELECT city FROM tab GROUP BY city) t").show();
        sqlContext.sql("SELECT firstname, lastname, email FROM tab").write().csv("file:///root/temp/output");
        sparkSession.catalog().dropTempView("tab");
    }

    public static void main( String[] args ) throws Exception {
        App app = new App();
        app.execute();
    }
}

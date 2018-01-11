package com.exkafka;

/**
 * Created by jhkwon78 on 2018-01-10.
 */
public class KafkaEnv {
    public static String bootstrapServers = "10.5.20.166:9092";
//    public static String bootstrapServers = "127.0.0.1:9092";
    public static String topicName = "test";
    public static int maxPollCount = 10;
    public static int maxProduceTestCount = 10;
    public static int maxConsumeTestCount = 10;
}

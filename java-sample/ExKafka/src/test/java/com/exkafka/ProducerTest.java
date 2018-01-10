package com.exkafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProducerTest {

    final static Logger logger = LoggerFactory.getLogger(ProducerTest.class);

    @Test
    public void _0_테스트준비() throws Exception {

    }

    @Test
    public void _1_테스트_ProducerMessage() throws Exception {
        Properties props = new Properties();
        props.put("bootstrap.servers", KafkaEnv.bootstrapServers);
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = new KafkaProducer<>(props);

        for(int i = 0; i < 500; i++) {
            Thread.sleep(60*1_000);
            //String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); // 2015-04-18 00:42:24
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")); // 00:42:24
            //producer.send(new ProducerRecord<String, String>(KafkaEnv.topicName, String.valueOf(i), now));
            producer.send(new ProducerRecord<String, String>(KafkaEnv.topicName, now));
            logger.info("*** produce {}", now);
        }

        producer.close();
    }
}

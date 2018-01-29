package com.exkafka;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BulkTest {

    final static long MAX_PUT_COUNT = 200_000L;
    final static Logger logger = LoggerFactory.getLogger(BulkTest.class);

    @Test
    public void _0_테스트준비() throws Exception {
    }

    @Test
    public void _1_테스트_ProducerMessage() throws Exception {
        String padding = StringUtils.leftPad("0", 1024);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        Properties props = new Properties();
        props.put("bootstrap.servers", KafkaEnv.bootstrapServers);
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        Producer<String, String> producer = new KafkaProducer<>(props);

        for(int i = 0; i < MAX_PUT_COUNT; i++) {
            producer.send(new ProducerRecord<String, String>(KafkaEnv.topicName, String.valueOf(i)+padding));
        }

        producer.close();
        stopWatch.stop();
        logger.info("elapsed time {} (secs)", stopWatch.getTime(TimeUnit.SECONDS));
    }
}

package com.exkafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConsumerTest {

    final static Logger logger = LoggerFactory.getLogger(ConsumerTest.class);

    @Test
    public void _0_테스트준비() throws Exception {

    }

    @Test
    public void _1_테스트_ConsumeMessage() throws Exception {
        Properties props = new Properties();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaEnv.bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put("auto.commit.interval.ms", "360000");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,         StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,      StringDeserializer.class.getName());

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(KafkaEnv.topicName));
        List<PartitionInfo> partitionInfos = consumer.partitionsFor(KafkaEnv.topicName);
        ConsumerRecords<String, String> records = consumer.poll(1000);
        List<String> values = new ArrayList<>();

        for (ConsumerRecord<String, String> record : records) {
            logger.info("p : {} o : {} k : {}  v : {}", record.partition(), record.offset(), record.key(), record.value());
            values.add(record.value());
        }

        logger.info("*** process batch consume {}", values);
        consumer.close();
    }
}

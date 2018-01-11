package com.exkafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.logging.Level;

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
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group-id");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put("auto.commit.interval.ms", "360000");
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "test-client-id");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,         StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,      StringDeserializer.class.getName());

        for (int i = 0; i < 500; i++) {
            logger.warn("<<< consume {}", getRecords(props));
            Thread.sleep(60_000);
        }
    }

    List<String> getRecords(Properties props) {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList(KafkaEnv.topicName));

        List<String> values = new ArrayList<>();
        int emptyCount = 0;

        for (int i = 0; i < 100 && emptyCount < 3; i++) {
            ConsumerRecords<String, String> records = consumer.poll(1000);

            if (records.count() == 0) {
                emptyCount++;
            } else {
                records.forEach(record -> {
                    values.add(record.value());
                });
            }
        }

        consumer.close();
        return values;
    }
}

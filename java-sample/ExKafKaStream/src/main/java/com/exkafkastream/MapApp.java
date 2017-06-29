package com.exkafkastream;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;

public class MapApp {
    final static Logger logger = LoggerFactory.getLogger(MapApp.class);
    public static void main( String[] args ) throws Exception {
        Thread streamThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Properties streamsConfiguration = new Properties();
                streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "mapapp-example");
                streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
                streamsConfiguration.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
                streamsConfiguration.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

                final Serde<String> stringSerde = Serdes.String();
                KStreamBuilder builder = new KStreamBuilder();
                KStream<String, String> textLines = builder.stream(stringSerde, stringSerde, "MapAppTopic");

                final KStream<String, String> uppercasedWithMap = textLines.map((key, value)-> {
                            return new KeyValue<>(key, value.toUpperCase());
                        });

                uppercasedWithMap.to(stringSerde, stringSerde, "UpperMapAppTopic");
                KafkaStreams streams = new KafkaStreams(builder, streamsConfiguration);
                streams.start();

                // Add shutdown hook to respond to SIGTERM and gracefully close Kafka Streams
                Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                    @Override
                    public void run() {
                        logger.info("close kafka stream");
                        streams.close();
                    }
                }));
            }
        });

        streamThread.start();
        Thread.sleep(2_000);

        Thread consumerThread = new Thread(new Runnable() {
            @Override
            public void run() {

                Properties props = new Properties();
                props.put("bootstrap.servers", "localhost:9092");
                props.put("group.id", "test");
                props.put("enable.auto.commit", "true");
                props.put("auto.commit.interval.ms", "1000");
                props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
                props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
                KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

                // 마지막부터 데이터를 읽어들임.
                consumer.subscribe(Arrays.asList("UpperMapAppTopic"));

                while (true) {
                    ConsumerRecords<String, String> records = consumer.poll(5000);
                    for (ConsumerRecord<String, String> record : records) {
                        logger.info("offset {} key {} value {}", record.offset(), record.key(), record.value());
                        // consumer.seek(new TopicPartition("test", 0), 5);
                    }
                }
            }
        });

        consumerThread.start();
        streamThread.join();
    }
}

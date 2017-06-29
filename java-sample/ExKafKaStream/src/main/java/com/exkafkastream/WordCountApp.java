package com.exkafkastream;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.KTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;

public class WordCountApp {
    final static Logger logger = LoggerFactory.getLogger(WordCountApp.class);
    public static void main( String[] args ) throws Exception {
        Thread streamThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Properties streamsConfiguration = new Properties();
                streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-lambda-example");
                streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
                streamsConfiguration.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
                streamsConfiguration.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

                final Serde<String> stringSerde = Serdes.String();
                final Serde<Long> longSerde = Serdes.Long();
                final Pattern pattern = Pattern.compile("\\W+", Pattern.UNICODE_CHARACTER_CLASS);
                KStreamBuilder builder = new KStreamBuilder();
                KStream<String, String> textLines = builder.stream(stringSerde, stringSerde, "TextLinesTopic");

                final KTable<String, Long> wordCounts = textLines
                        // Split each text line, by whitespace, into words.  The text lines are the record
                        // values, i.e. we can ignore whatever data is in the record keys and thus invoke
                        // `flatMapValues()` instead of the more generic `flatMap()`.
                        .flatMapValues(value -> {
                            logger.info("value {}", value);
                            return Arrays.asList(pattern.split(value.toLowerCase()));
                        })
                        // Count the occurrences of each word (record key).
                        //
                        // This will change the stream type from `KStream<String, String>` to `KTable<String, Long>`
                        // (word -> count).  In the `count` operation we must provide a name for the resulting KTable,
                        // which will be used to name e.g. its associated state store and changelog topic.
                        //
                        // Note: no need to specify explicit serdes because the resulting key and value types match our default serde settings
                        .groupBy((key, word) -> {
                            logger.info("groupBy {}", word);
                            return word;
                        })
                        .count("Counts");

                wordCounts.to(stringSerde, longSerde, "WordsWithCountsTopic");
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
                props.put("value.deserializer", "org.apache.kafka.common.serialization.LongDeserializer");
                KafkaConsumer<String, Long> consumer = new KafkaConsumer<>(props);

                // 마지막부터 데이터를 읽어들임.
                consumer.subscribe(Arrays.asList("WordsWithCountsTopic"));

                while (true) {
                    ConsumerRecords<String, Long> records = consumer.poll(5000);
                    for (ConsumerRecord<String, Long> record : records) {
                        logger.info("offset {} key {} value {}", record.offset(), record.key(), record.value().longValue());
                        // consumer.seek(new TopicPartition("test", 0), 5);
                    }
                }
            }
        });

        consumerThread.start();
        streamThread.join();
    }
}

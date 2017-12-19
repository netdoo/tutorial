package com.exredis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Map;
import java.util.Set;

public class App {

    final static Logger logger = LoggerFactory.getLogger(App.class);

    static void doKeyValue(SpringRedis redis) {

        final String key = "001";

        redis.setValue(key, "hello");
        redis.setValue(key, "world");

        logger.info("{}", redis.getValue(key));

        redis.delKey(key);

        logger.info("{}", redis.getValue(key));
    }

    static void doHashKeyValue(SpringRedis redis) {

        redis.hashSetValue("MBC", "CHANNEL", "11");
        redis.hashSetValue("MBC", "RADIO", "95.9MHz");

        redis.hashSetValue("SBS", "CHANNEL", "06");
        redis.hashSetValue("SBS", "RADIO", "103.5MHz");

        logger.info("MBC RADIO {}", redis.hashGetValue("MBC", "RADIO"));
        logger.info("SBS RADIO {}", redis.hashGetValue("SBS", "RADIO"));

        logger.info("=== find MBC entries ===");
        Map<Object, Object> entries = redis.hashEntries("MBC");
        entries.forEach((key, value) -> {
            logger.info("{} {}", key, value);
        });

        redis.hashDelValue("MBC", "RADIO");

        logger.info("=== find MBC entries 2 ===");
        Map<Object, Object> entries2 = redis.hashEntries("MBC");
        entries2.forEach((key, value) -> {
            logger.info("{} {}", key, value);
        });
    }

    static void doRank(SpringRedis redis) {

        final String key = "colorRank";

        redis.zIncrement(key, "red", 1);
        redis.zIncrement(key, "red", 1);
        redis.zIncrement(key, "red", 1);

        redis.zIncrement(key, "green", 1);
        redis.zIncrement(key, "green", 1);
        redis.zIncrement(key, "green", 1);

        redis.zIncrement(key, "blue", 1);
        redis.zIncrement(key, "blue", 1);
        redis.zIncrement(key, "blue", 1);

        redis.zIncrement(key, "black", 1);
        redis.zIncrement(key, "white", 1);
        redis.zIncrement(key, "navy", 1);
        redis.zIncrement(key, "pink", 1);

        redis.zAdd(key, "gold", 100_000);
        redis.zAdd(key, "gold", 200_000);

        Set<Object> rank = redis.getTop(key, 3);
        logger.info("rank {}", rank);

        logger.info("======= get top 3 =======");

        Set<ZSetOperations.TypedTuple<Object>> rankWithScore = redis.getTopWithScore(key, 3);
        rankWithScore.forEach(tuple -> {
            logger.info("{} {}", tuple.getValue(), tuple.getScore());
        });


        logger.info("======= get top 3 ~ 5 =======");

        // 3위부터 5위까지 출력
        rankWithScore = redis.getTopWithScore(key, 3, 5);
        rankWithScore.forEach(tuple -> {
            logger.info("{} {}", tuple.getValue(), tuple.getScore());
        });
    }

    public static void main( String[] args ) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        context.register(AppConfig.class);
        context.refresh();

        SpringRedis springRedis = context.getBean("springRedis", SpringRedis.class);
        springRedis.flushAll();

        doKeyValue(springRedis);
        doHashKeyValue(springRedis);
        doRank(springRedis);
    }
}

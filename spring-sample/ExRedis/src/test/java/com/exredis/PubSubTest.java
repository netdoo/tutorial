package com.exredis;

import com.exredis.config.AppConfig;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PubSubTest {

    @Autowired
    SpringRedis springRedis;

    @Autowired
    RedisMessageListenerContainer redisMessageListenerContainer;

    final static String MBC = "MBC";
    final static String SBS = "SBS";

    final static Logger logger = LoggerFactory.getLogger(PubSubTest.class);

    @Test
    public void _0_테스트_준비() {
        springRedis.flushAll();

        // Subscribe to MBC
        redisMessageListenerContainer.addMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {

                String channel = new String(message.getChannel());
                String body = new String(message.getBody());

                logger.info("recv #1 {} {}", channel, body);
            }
        }, new ChannelTopic(MBC));

        // Subscribe to SBS
        redisMessageListenerContainer.addMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                String channel = new String(message.getChannel());
                String body = new String(message.getBody());

                logger.info("recv #2 {} {}", channel, body);
            }
        }, new ChannelTopic(SBS));
    }

    @Test
    public void _1_PUB_SUB_테스트() throws Exception {
        logger.info("publish message");
        this.springRedis.publish(MBC, "한글");
        this.springRedis.publish(SBS, "Korea");
    }

    @Test
    public void _99_테스트_종료() throws Exception {

    }
}

package com.exredis;

import com.exredis.config.AppConfig;
import com.exredis.domain.Box;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ObjectTest {

    @Autowired
    SpringRedis springRedis;

    final static Logger logger = LoggerFactory.getLogger(ObjectTest.class);

    @Test
    public void _0_테스트_준비() {
        springRedis.flushAll();
    }

    @Test
    public void _1_OBJECT_KEY_VALUE_테스트() throws Exception {
        this.springRedis.setObject("001", new Box("001", "RED", Arrays.asList("RED", "DARKRED", "LIGHTRED")));
        Box box = this.springRedis.getObject("001");
        logger.info("{}", box);
    }

    @Test
    public void _99_테스트_종료() throws Exception {

    }
}

package com.exredis;

import com.exredis.config.AppConfig;
import com.exredis.domain.Box;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ListTest {

    @Autowired
    SpringRedis springRedis;

    final static Logger logger = LoggerFactory.getLogger(ListTest.class);

    @Test
    public void _0_테스트_준비() {
        springRedis.flushAll();
    }

    @Test
    public void _1_LIST_PUSH_POP_테스트() throws Exception {

        this.springRedis.leftPush("001", "RED");
        logger.info("{}", this.springRedis.listIndex("001", 0));
        this.springRedis.listSet("001", 0, "DarkRed");
        logger.info("{}", this.springRedis.listIndex("001", 0));
        logger.info("{}", this.springRedis.leftPop("001"));
    }

    @Test
    public void _2_LIST_RANGE_테스트() throws Exception {
        this.springRedis.leftPush("001", "RED");
        this.springRedis.leftPush("001", "GREEN");
        this.springRedis.leftPush("001", "BLUE");
        List<String> all = this.springRedis.listRange("001", 0, -1);
        List<String> part = this.springRedis.listRange("001", 1, 2);
        logger.info("{} {}", all, part);
    }

    @Test
    public void _99_테스트_종료() throws Exception {

    }
}

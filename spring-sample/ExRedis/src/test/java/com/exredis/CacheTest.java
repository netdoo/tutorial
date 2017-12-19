package com.exredis;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;
import java.util.Set;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CacheTest {

    @Autowired
    SpringRedis springRedis;

    @Autowired
    CacheManager cacheManager;

    @Autowired
    ColorDB colorDB;

    final static Logger logger = LoggerFactory.getLogger(CacheTest.class);

    @Test
    public void _0_테스트_준비() {
        springRedis.flushAll();
    }

    @Test
    public void _1_캐쉬_테스트() throws Exception {
        Cache cache = this.cacheManager.getCache("myCache");

        cache.put("001", "RED");
        cache.put("002", "GREEN");

        cache.evict("002");
        assertThat(cache.get("002", String.class), is(nullValue()));

        assertThat(cache.get("001", String.class), is("RED"));
        Thread.sleep(12_000);
        assertThat(cache.get("001", String.class), is(nullValue()));
    }

    @Test
    public void _2_캐쉬_테스트() throws Exception {
        this.colorDB.putColor("001");
        logger.info("{}", this.colorDB.getColor("001"));
        logger.info("{}", this.colorDB.getColor("001"));
        logger.info("{}", this.colorDB.getColor("001"));
    }
}

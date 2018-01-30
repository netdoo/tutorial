package com.exmemcacheweb;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import static junit.framework.TestCase.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JedisTest {
    final static Logger logger = LoggerFactory.getLogger(JedisTest.class);
    final static String host = "localhost";
    final static int port = 6379;

    @Test
    public void _0_테스트_준비() throws Exception {

    }

    @Test
    public void _1_Jedis_테스트() throws Exception {
        Jedis jedis = new Jedis(host, port);
        jedis.set("key", "value");
        String value = jedis.get("key");
        logger.info("{}", value);
        jedis.close();
    }

    @Test
    public void _2_Pool_테스트() throws Exception {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        JedisPool pool = new JedisPool(jedisPoolConfig, host, port, 1000);

        Jedis jedis = pool.getResource();

        jedis.set("foo", "bar");
        String value = jedis.get("foo");
        assertTrue( value != null && value.equals("bar") );

        jedis.del("foo");
        value = jedis.get("foo");
        assertTrue(value == null);

        if( jedis != null ){
            jedis.close();
        }
    }

    @Test
    public void _3_Expire_Timeout_테스트() throws Exception {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        JedisPool pool = new JedisPool(jedisPoolConfig, host, port, 1000);

        Jedis jedis = pool.getResource();

        jedis.set("key", "value");
        jedis.expire("key", 3); // 해당 입력하는 값은 초(sec) 단위입니다.

        for (int i = 0; i < 5; i++) {
            logger.info("{}", jedis.get("key"));
            Thread.sleep(1000);
        }

        if( jedis != null ){
            jedis.close();
        }
    }
}

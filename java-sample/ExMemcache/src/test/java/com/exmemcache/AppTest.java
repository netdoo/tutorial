package com.exmemcache;

import com.exmemcache.config.MemcachedConfig;
import net.spy.memcached.MemcachedClient;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MemcachedConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest {

    final static Logger logger = LoggerFactory.getLogger(AppTest.class);

    @Autowired
    MemcachedClient memcachedClient;

    @BeforeClass
    public static void onBeforeClass() throws Exception {

    }

    @Test
    public void _0_테스트_준비() throws Exception {

    }

    @Test
    public void _01_CRUDTest() throws Exception {
        int expireSecs = 5;
        String key = "001";
        String value = "RED";

        memcachedClient.set(key, expireSecs, value);
        logger.info("set {}/{}", key, value);

        value = (String)memcachedClient.get(key);
        logger.info("get {}/{}", key, value);

        memcachedClient.set(key, expireSecs, "Red");
        logger.info("overwrite {}/{}", key, "Red");

        value = (String)memcachedClient.get(key);
        logger.info("get {}/{}", key, value);

        switch (memcachedClient.delete(key).getStatus().getStatusCode()) {
        case ERR_NOT_FOUND:
            logger.error("not found {}", key);
            break;
        case SUCCESS:
            logger.info("delete {}", key);
            break;
        }

        switch (memcachedClient.delete(key).getStatus().getStatusCode()) {
            case ERR_NOT_FOUND:
                logger.error("not found {}", key);
                break;
            case SUCCESS:
                logger.info("delete {}", key);
                break;
        }

        logger.info("delete {}", key);

        value = (String)memcachedClient.get(key);
        logger.info("get {}/{}", key, value);
    }

    @Test
    public void _02_TimeoutTest() throws Exception {
        int expireSecs = 5;
        String key = "001";
        String value = "RED";

        memcachedClient.set(key, expireSecs, value);
        logger.info("set {}/{}", key, value);

        value = (String)memcachedClient.get(key);
        logger.info("get {}/{}", key, value);

        TimeUnit.SECONDS.sleep(expireSecs+1);
        logger.info("after timeout...");

        value = (String)memcachedClient.get(key);
        logger.info("get {}/{}", key, value);
    }

    @Test
    public void _03_BulkSet() throws Exception {
        int expireSecs = 5;
        memcachedClient.set("001", expireSecs, "RED");
        memcachedClient.set("002", expireSecs, "GREEN");
        memcachedClient.set("003", expireSecs, "BLUE");
        memcachedClient.set("004", expireSecs, "WHITE");
        memcachedClient.set("005", expireSecs, "BLACK");
    }
}

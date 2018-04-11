package com.exobjectpool;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest {

    @Test
    public void _01_ObjectPoolTest() throws Exception {

        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(10);             // Object Pool 의 사이즈
        config.setMaxWaitMillis(5_000);     // borrow 타임 아웃시간을 설정

        ObjectPool<StringBuffer> objectPool = new GenericObjectPool<>(new CustomPoolFactory(), config);
        StringBuffer buffer = null;

        try {
            // Object Pool 에서 객체를 할당받음.
            buffer = objectPool.borrowObject();
            logger.info("borrow object {}", buffer);
            buffer.append("Hello Buffer");
        } catch (Exception e) {
            logger.error("borrowObject error : ", e);
        } finally {
            try {
                if (null != buffer) {
                    // Object Pool 에서 할당받았던 객체를 반납함.
                    objectPool.returnObject(buffer);
                    logger.info("return object {}", buffer);
                }
            } catch (Exception e) {
                logger.error("returnObject error : ", e);
            }
        }
    }

    Logger logger = LoggerFactory.getLogger(getClass());
}

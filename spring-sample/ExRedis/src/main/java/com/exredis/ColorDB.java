package com.exredis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ColorDB {

    // https://github.com/caseyscarborough/spring-redis-caching-example/blob/master/src/main/java/com/caseyscarborough/spring/redis/config/CacheConfig.java

    Map<String, String> colorDB = new HashMap<>();
    final static Logger logger = LoggerFactory.getLogger(ColorDB.class);

    public ColorDB() {
        this.colorDB.put("001", "RED");
        this.colorDB.put("002", "GREEN");
        this.colorDB.put("003", "BLUE");
        this.colorDB.put("004", "WHITE");
        this.colorDB.put("005", "BLACK");
    }

    /**
     * 캐쉬 갱신
     */
    @CachePut(value = "colors")
    public String putColor(String id) {
        logger.info("read from colorDB for update the cache {}", id);
        return this.colorDB.get(id);
    }

    /**
     * 캐쉬 조회, 데이터 조회후 캐쉬에 추가
     */
    @Cacheable(value = "colors")
    public String getColor(String id) {
        logger.info("read from colorDB {}", id);
        return this.colorDB.get(id);
    }

    /**
     * 캐쉬 삭제
     */
    @CacheEvict("colors")
    public void delColor(String id) {
        logger.info("delColor {}", id);
    }
}

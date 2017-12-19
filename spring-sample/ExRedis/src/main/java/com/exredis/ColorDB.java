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
     * The User that is saved from this method will be stored in the
     * cache and referenced by its' ID.
     */
    @CachePut("colors")
    public String putColor(String id) {
        logger.info("putColor {}", id);
        return this.colorDB.get(id);
    }

    /**
     * This method should never actually be executed, since the User will
     * always be retrieved from the cached output of saveUser.
     */
    @Cacheable("colors")
    public String getColor(String id) {
        logger.info("getColor {}", id);
        return this.colorDB.get(id);
    }

    /**
     * When this method is called, the cached User will be deleted from
     * the cache.
     */
    @CacheEvict("colors")
    public void delColor(String id) {
        logger.info("delColor {}", id);
    }
}

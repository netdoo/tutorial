package com.exguava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.TimeUnit;

public class App {

    final static Logger logger = LoggerFactory.getLogger(App.class);

    static void sleepMillSec(int millisecs) {
        try {
            Thread.sleep(millisecs);
        } catch (Exception e) {}
    }

    public static void main( String[] args ) {

        CacheLoader<String, String> cacheLoader =
                new CacheLoader<String, String>() {
                    @Override
                    public String load(String key) throws Exception {
                        logger.info("make cache {}", key);
                        return key.toUpperCase();
                    }
                };

        // 최대 3개까지 캐쉬를 유지하고, 500 밀리초 이후 갱신됨.
        LoadingCache<String, String> cache =
                CacheBuilder.newBuilder()
                        .maximumSize(3)
                        .expireAfterAccess(500, TimeUnit.MILLISECONDS)
                        .build(cacheLoader);

        logger.info("1. {}", cache.getUnchecked("aaa"));
        logger.info("2. {}", cache.getUnchecked("bbb"));
        logger.info("3. {}", cache.getUnchecked("ccc"));
        logger.info("현재 cache {}", cache.asMap().toString());

        logger.info("최대 3개 까지만, 캐쉬가 유지되니, 4번째 캐쉬가 입력되면, 가장 처음 입력된 캐쉬가 삭제된다.");
        logger.info("4. {}", cache.getUnchecked("ddd"));
        logger.info("현재 cache {}", cache.asMap().toString());

        logger.info("500밀리초가 경과했으니, 모든 캐쉬가 삭제된다.");
        sleepMillSec(500);

        logger.info("현재 cache {}", cache.asMap().toString());
    }
}

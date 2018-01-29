package com.exmemcacheweb.service;

import com.exmemcacheweb.controller.MainController;
import com.google.code.ssm.api.InvalidateSingleCache;
import com.google.code.ssm.api.ParameterValueKeyProvider;
import com.google.code.ssm.api.ReadThroughSingleCache;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CacheService {
    final String MEMCACHE_NAMESPACE = "MyCacheSpace";

    final static Logger logger = LoggerFactory.getLogger(CacheService.class);

    // expiration : 초단위
    @ReadThroughSingleCache(namespace = MEMCACHE_NAMESPACE, expiration = 10)
    public String getSomeData(@ParameterValueKeyProvider Long id) throws Exception {
        String padding = StringUtils.leftPad("A", 4096, "0");
        logger.info("read from cache. {}", id);
        return padding;
    }

    @InvalidateSingleCache(namespace = MEMCACHE_NAMESPACE)
    public void removeCache(@ParameterValueKeyProvider long id) {
        logger.info("캐쉬가 제거됨 ID : " + id);
    }
}

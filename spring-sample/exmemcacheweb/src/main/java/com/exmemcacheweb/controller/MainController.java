package com.exmemcacheweb.controller;

import com.exmemcacheweb.model.MainParam;
import com.exmemcacheweb.service.CacheService;
import com.google.code.ssm.api.ParameterValueKeyProvider;
import com.google.code.ssm.api.ReadThroughSingleCache;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;

@Controller
public class MainController {

    @Autowired
    CacheService cacheService;

    @Autowired
    JedisPool jedisPool;

    final static Logger logger = LoggerFactory.getLogger(MainController.class);

    @PostConstruct
    public void setDefaultCacheData() {
        Jedis jedis = jedisPool.getResource();
        String padding = StringUtils.leftPad("A", 4096, "0");
        jedis.set("key", padding);
        jedis.close();
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @ResponseBody
    public String hello(MainParam mainParam) {
        logger.info("Param {}", mainParam);
        return "Hello World " + mainParam.getName();
    }

    @RequestMapping(value = "/mcbulk", method = RequestMethod.GET)
    @ResponseBody
    public String mcbulk(@RequestParam(value="id", required=false, defaultValue="1") Long id) throws Exception {
        String s =  cacheService.getSomeData(id);
        return s;
    }

    @RequestMapping(value = "/rebulk", method = RequestMethod.GET)
    @ResponseBody
    public String rebulk(@RequestParam(value="id", required=false, defaultValue="1") Long id) throws Exception {
        Jedis jedis = jedisPool.getResource();
        String s = jedis.get("key");
        jedis.close();
        return s;
    }
}



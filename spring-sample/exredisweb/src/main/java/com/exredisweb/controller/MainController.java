package com.exredisweb.controller;

import com.exredisweb.model.MainParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;

@Controller
public class MainController {


    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    final static Logger logger = LoggerFactory.getLogger(MainController.class);


    @PostConstruct
    public void init() {
        String padding = StringUtils.leftPad("A", 4096, "0");
        this.redisTemplate.opsForValue().set("foo", padding);
    }


    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @ResponseBody
    public String hello(MainParam mainParam) {
        logger.info("Param {}", mainParam);
        return "Hello World " + mainParam.getName();
    }

    @RequestMapping(value = "/bulk", method = RequestMethod.GET)
    @ResponseBody
    public String bulk() {
        String s =  (String)this.redisTemplate.opsForValue().get("foo");
        return s;
    }
}



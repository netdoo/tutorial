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

@Controller
public class MainController {

    @Autowired
    CacheService cacheService;

    final static Logger logger = LoggerFactory.getLogger(MainController.class);

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @ResponseBody
    public String hello(MainParam mainParam) {
        logger.info("Param {}", mainParam);
        return "Hello World " + mainParam.getName();
    }

    @RequestMapping(value = "/bulk", method = RequestMethod.GET)
    @ResponseBody
    public String bulk(@RequestParam(value="id", required=false, defaultValue="1") Long id) throws Exception {
        String s =  cacheService.getSomeData(id);
        return s;
    }
}



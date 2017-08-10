package com.exjpa.controller;

import com.exjpa.repository.MemoRepository;
import com.exjpa.repository.MyRepository;
import com.exjpa.properties.DataProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MainController {

    final static Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    MyRepository myRepository;

    @Autowired
    MemoRepository memoRepository;

    @Autowired
    DataProperties dataProperties;

    @Value("${data.server}")
    String dataServer;

    @RequestMapping(method = RequestMethod.GET, value = "/hello")
    String hello() {
        return "hello world";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/world", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ResponseBody
    Object world() {
        Map<String, String> response = new HashMap<>();
        response.put("greeting", "hello world");
        return response;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/repository", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ResponseBody
    Object repository() {
        return myRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/dataserver", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ResponseBody
    Object dataserver() {
        logger.info("==> dataserver");
        return dataServer;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/dataprop", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ResponseBody
    Object dataprop() {
        logger.info("==> dataprop");
        return dataProperties;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/memo", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ResponseBody
    Object memo() {
        logger.info("==> memo");
        return memoRepository.findAll();
    }
}

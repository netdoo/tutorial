package com.exsimpleweb2.controller;

import com.exsimpleweb2.domain.Hello;
import com.exsimpleweb2.model.MainParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class MainController {

    final static Logger logger = LoggerFactory.getLogger(MainController.class);

    @RequestMapping(value = "/hello", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Hello hello() {
        return new Hello("hello world");
    }

    @RequestMapping(value = "/world", method = RequestMethod.GET)
    @ResponseBody
    public String world(MainParam mainParam) {
        logger.info("Param {}", mainParam);
        return mainParam.getName();
    }
}



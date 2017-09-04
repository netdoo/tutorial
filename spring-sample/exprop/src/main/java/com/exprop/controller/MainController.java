package com.exprop.controller;

import com.exprop.domain.Hello;
import com.exprop.domain.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@PropertySource("classpath:/config.properties")
public class MainController {

    final static Logger logger = LoggerFactory.getLogger(MainController.class);

    @Value("${greeting}")
    String greeting;

    @Autowired
    World world;

    @RequestMapping(value = "/hello", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Hello hello() {
        return new Hello("hello world");
    }

    @RequestMapping(value = "/world", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public World world() {
        return world;
    }
}

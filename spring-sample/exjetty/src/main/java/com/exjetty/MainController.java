package com.exjetty;

import com.exjetty.model.MainParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Random;

@Controller
public class MainController {

    final static Logger logger = LoggerFactory.getLogger(MainController.class);

    @RequestMapping(value = "/jetty", method = RequestMethod.GET)
    @ResponseBody
    public String hello(MainParam mainParam) {
        System.out.println(mainParam);
        return "Hello jetty";
    }

    @RequestMapping(value = "/jetty2", method = RequestMethod.GET)
    @ResponseBody
    public String jetty2(MainParam mainParam) {
        System.out.println(mainParam);
        return "Hello jetty222";
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public String hello() {
        return "Hello hello";
    }

    @RequestMapping(value="/exception", method = RequestMethod.GET)
    @ResponseBody
    public String exception(@RequestParam(value="message", required=false, defaultValue="1") String message) throws Exception {
        throw new IllegalArgumentException("exception raised");
    }
}


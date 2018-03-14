package com.exjsp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ColorController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping(value = "/red")
    public String red() {
        logger.info("request /red");
        return "red";
    }

    @GetMapping(value = "/green")
    public ModelAndView green() {

        logger.info("request /green");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("green");
        return modelAndView;
    }

    @RequestMapping(value = "/blue", method = RequestMethod.GET)
    public String blue() {
        logger.info("request /blue");
        return "blue";
    }
}

package com.extiles.controller;

import com.extiles.model.User;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Controller
public class MainController {
    final static Logger logger = LoggerFactory.getLogger(MainController.class);

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView dashboard() {
        ModelAndView model = new ModelAndView();
        model.addObject("users", Arrays.asList(new User("foo"), new User("bar"), new User("zoo")));
        model.setViewName("users");
        return model;
    }

    /// http://localhost:8080/home/intro
    @RequestMapping(value = "/home/intro", method = RequestMethod.GET)
    public String home() {
        return "home/intro";
    }

    /// http://localhost:8080/my/first/homepage
    @RequestMapping(value = "/my/first/homepage", method = RequestMethod.GET)
    public String homepage() {
        return "my/first/homepage";
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        return "hello";
    }

    @RequestMapping(value = "/exception", method = RequestMethod.GET)
    public String exception() {
        throw new InternalException("my custome internal exception");
    }
}

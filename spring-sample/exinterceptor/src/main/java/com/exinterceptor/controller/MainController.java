package com.exinterceptor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    final String greeting = "Hello World";

    @RequestMapping(value = "/greeting", method = RequestMethod.GET)
    @ResponseBody
    public String greeting() {
        return greeting;
    }

    @RequestMapping(value = "/lower", method = RequestMethod.GET)
    @ResponseBody
    public String lower() {
        return greeting;
    }

    @RequestMapping(value = "/upper", method = RequestMethod.GET)
    @ResponseBody
    public String upper() {
        return greeting;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    public String login() {
        return "Not Authorized";
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    @ResponseBody
    public String home() {
        return "Welcome home";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    @ResponseBody
    public String admin() {
        return "Welcome admin";
    }
}

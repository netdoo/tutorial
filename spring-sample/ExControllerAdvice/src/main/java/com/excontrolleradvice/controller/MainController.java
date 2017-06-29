package com.excontrolleradvice.controller;

import com.excontrolleradvice.exception.BarException;
import com.excontrolleradvice.exception.FooException;
import com.excontrolleradvice.exception.RestException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @ResponseBody
    public String hello() {
        return "Hello Word";
    }

    @RequestMapping(value = "/foo", method = RequestMethod.GET)
    @ResponseBody
    public void foo() {
        throw new FooException("foo exception raised");
    }

    @RequestMapping(value = "/bar", method = RequestMethod.GET)
    @ResponseBody
    public void bar() {
        throw new BarException("bar exception raised");
    }

    @RequestMapping(value = "/etc", method = RequestMethod.GET)
    @ResponseBody
    public void etc() {
        throw new RuntimeException("etc exception raised");
    }
}

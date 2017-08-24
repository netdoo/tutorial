package com.exsimpleweb2.controller;

import com.exsimpleweb2.domain.Hello;
import com.exsimpleweb2.model.MainParam;
import com.exsimpleweb2.service.MyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Controller
public class MainController {

    final static Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    MyService myService;

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

    @RequestMapping(value = "/myservice", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<Object> myservice() throws Exception {

        CompletableFuture<String> foo = this.myService.getFoo();
        CompletableFuture<String> bar = this.myService.getBar();
        CompletableFuture<String> zoo = this.myService.getZoo();

        CompletableFuture.allOf(foo, bar, zoo).join();

        Map<String, Object> response = new HashMap<>();
        response.put("foo", foo.get());
        response.put("bar", bar.get());
        response.put("zoo", zoo.get());

        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }
}



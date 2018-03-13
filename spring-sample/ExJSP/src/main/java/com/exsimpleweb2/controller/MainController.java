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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @RequestMapping(value = "/site1/{q}", method = RequestMethod.GET)
    @ResponseBody
    public String site1(@PathVariable("q") String q) {
        logger.info("Site1 : query : {}", q);
        return q;
    }

    @RequestMapping(value = "/site2/{q:.+}", method = RequestMethod.GET)
    @ResponseBody
    public String site2(@PathVariable("q") String q) {
        logger.info("Site2 : query : {}", q);
        return q;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public String save(HttpServletRequest request) {
        logger.info("name : {}", request.getParameter("name"));
        return "ok";
    }

    @RequestMapping(value = "/save2", method = RequestMethod.POST)
    @ResponseBody
    public String save2(Hello hello) {
        logger.info("name : {}", hello.getResult());
        return "ok2";
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



package com.exjsp.controller;

import com.exjsp.domain.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class FormController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @PostMapping(value = "/save", consumes = "application/x-www-form-urlencoded")
    public String save(@RequestBody Member member) {
        logger.info("member.name {}", member.getName());
        return "home";
    }

    @PostMapping(value = "/put", consumes = "application/x-www-form-urlencoded", produces = "application/json")
    @ResponseBody
    public Member put(HttpServletRequest request) {
        String name = Optional.ofNullable(request.getParameter("name")).orElse("name is empty");
        Integer age = Integer.valueOf(Optional.ofNullable(request.getParameter("age")).orElse("0"));
        logger.info("name {}, age {}", name, age);
        return new Member(name, age);
    }

    @GetMapping(value = "/home2")
    public ModelAndView home2() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        return modelAndView;
    }
}

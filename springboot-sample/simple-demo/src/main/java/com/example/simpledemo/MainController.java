package com.example.simpledemo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @RequestMapping("/")
    String greetings() {
        return "<h1>Hello Spring Boot</h1>";
    }
}

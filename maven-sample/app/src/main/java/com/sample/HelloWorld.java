package com.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/// Sample Bean
public class HelloWorld {
    private String name;

    Logger logger = LoggerFactory.getLogger(getClass());

    public void setName(String name) {
        this.name = name;
    }
 
    public void printHello() {
        logger.info("Hello {}", name);
    }
}

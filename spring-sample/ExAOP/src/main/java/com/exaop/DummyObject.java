package com.exaop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class DummyObject {

    final Logger logger = LoggerFactory.getLogger(getClass());

    String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void printName() {
        logger.info("DummyObject => name {}", this.name);
    }

    public void printName(String name) {
        logger.info("DummyObject => name 2 {}", name);
    }

    @LogExecutionTime
    public void writeDelayedLog(String text) throws Exception {
        TimeUnit.SECONDS.sleep(3);
        logger.info("delayed log");
    }

    public void close() {
        logger.info("DummyObject => close");
    }
}

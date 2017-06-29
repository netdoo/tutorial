package com.exaop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SomeObject {

    final Logger logger = LoggerFactory.getLogger(getClass());

    public void doSomething() {
        logger.info("SomeObject => do something");
    }

    public void doNothing() {
        logger.info("SomeObject => do nothing");
    }
}

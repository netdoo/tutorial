package com.exasynclogger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    final static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) {
        for (int i = 0; i < 100000; i++) {
            logger.info("Hello World " + i);
        }
    }
}


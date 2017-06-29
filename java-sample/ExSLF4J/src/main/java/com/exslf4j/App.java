package com.exslf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    final static Logger logger = LoggerFactory.getLogger(App.class);
    public static void main( String[] args ) {

        /// 동적으로 로그레밸 설정
        org.apache.log4j.Logger logger4j = org.apache.log4j.Logger.getRootLogger();
        logger4j.setLevel(org.apache.log4j.Level.toLevel("INFO"));

        logger.debug("debug number is {} ", 777);
        logger.info("info number is {} ", 778);
    }
}

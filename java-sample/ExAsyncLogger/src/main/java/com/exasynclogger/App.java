package com.exasynclogger;

import org.apache.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    final static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) {

        for (int i = 0; i < 10; i++) {
            logger.info("Hello World " + i);
        }

        // 로그가 비동기로 기록되기 때문에, 프로그램 종료 직전에
        // 반드시 다음과 같이 로그를 모두 다 플러쉬 해줘야 한다.
        // flush all logs
        LogManager.shutdown();
    }
}


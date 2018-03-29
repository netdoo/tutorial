package com.exaop;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class AppLife  {

    public AppLife() {
        logger.info("시작");
    }

    @PreDestroy
    public void destroy() throws Exception {
        stopWatch.stop();
        logger.info("종료 경과시간 {}", stopWatch);
    }

    StopWatch stopWatch = StopWatch.createStarted();
    final static Logger logger = LoggerFactory.getLogger(AppLife.class);
}

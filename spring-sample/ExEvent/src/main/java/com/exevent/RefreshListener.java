package com.exevent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class RefreshListener implements ApplicationListener<ContextRefreshedEvent> {

    final static Logger logger = LoggerFactory.getLogger(RefreshListener.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent cse) {
        logger.info("Handling context re-freshed event. ");
    }
}

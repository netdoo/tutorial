package com.exevent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SomePublisher {

    final static Logger logger = LoggerFactory.getLogger(SomePublisher.class);

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishEvent(final String message) {
        logger.info("Publishing custom event. {}", message);
        SomeEvent someEvent = new SomeEvent(this, message);
        applicationEventPublisher.publishEvent(someEvent);
    }
}

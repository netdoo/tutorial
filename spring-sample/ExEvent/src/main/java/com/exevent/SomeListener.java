package com.exevent;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class SomeListener implements ApplicationListener<SomeEvent> {

    final static Logger logger = LoggerFactory.getLogger(SomeListener.class);

    @Override
    public void onApplicationEvent(SomeEvent event) {
        logger.info("Received spring custom event {} ", event.getMessage());
    }
}

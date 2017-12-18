package com.exevent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

    final static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        context.register(AppConfig.class);
        context.refresh();

        SomePublisher somePublisher = context.getBean("somePublisher", SomePublisher.class);
        somePublisher.publishEvent("Hello Spring Event");
    }
}

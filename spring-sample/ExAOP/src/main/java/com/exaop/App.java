package com.exaop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

    final static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        context.register(AppConfig.class);
        context.refresh();

        SomeObject someObject = (SomeObject)context.getBean("someObject");
        DummyObject dummyObject = (DummyObject)context.getBean("dummyObject");

        someObject.doSomething();
        dummyObject.printName();
    }
}

package com.exprofile;

import com.exprofile.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

    static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        context.getEnvironment().setActiveProfiles("real");
        context.register(AppConfig.class);
        context.refresh();

        String myName = (String)context.getBean("myName");
        logger.info("myName {}", myName);
        ((ConfigurableApplicationContext) context).close();
    }
}


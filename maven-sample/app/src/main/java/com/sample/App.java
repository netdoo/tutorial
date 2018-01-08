package com.sample;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    final static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) {
         String[] springConfig =
            {
                "classpath:applicationContext.xml"
            };

        ClassPathXmlApplicationContext context = null;

        try {
            context = new ClassPathXmlApplicationContext(springConfig);
            Properties prop = new Properties();
            prop.load(HelloWorld.class.getClassLoader().getResourceAsStream("db.properties"));

            logger.info("{} {} {}", prop.getProperty("host"), prop.getProperty("name"), prop.getProperty("port"));
            HelloWorld helloWorld = context.getBean(HelloWorld.class);
            helloWorld.printHello();
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            if (null != context) {
                context.close();
            }
        }
    }
}

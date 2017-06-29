package com.sample;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App 
{
    public static void main( String[] args )
    {
         String[] springConfig =
                {
                        "classpath:applicationContext.xml"
                };

        ClassPathXmlApplicationContext context = null;

        try {

            context = new ClassPathXmlApplicationContext(springConfig);

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

package com.exresource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;

public class App {
    final static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) {

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(classloader.getResourceAsStream("log4j.properties")));) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                logger.info("{}", line);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }
}


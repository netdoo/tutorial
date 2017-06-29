package com.exjava;

import org.apache.log4j.Logger;

public class ExThread {
    protected static Logger logger = Logger.getLogger(ExThread.class);

    public static void normal() {
        Thread t = new Thread(new Runnable() {
            public void run() {
                logger.info("Normal Thread Working ...");
            }
        });

        t.start();

        try {
            logger.info("Normal Thread isAlive : " + t.isAlive());
            t.join();
            logger.info("Normal Thread isAlive : " + t.isAlive());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void lambda() {
        Thread t = new Thread(()->logger.info("Lambda Working ..."));

        t.start();
        try {
            logger.info("Lambda Thread isAlive : " + t.isAlive());
            t.join();
            logger.info("Lambda Thread isAlive : " + t.isAlive());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


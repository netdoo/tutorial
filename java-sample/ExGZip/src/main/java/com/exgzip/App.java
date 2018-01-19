package com.exgzip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;

public class App {
    final static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) throws Exception {
        logger.info("start gzip");
        RunShell.RunSh("gzip", Arrays.asList("-f", "/root/temp/test.txt"), true);
        logger.info("finish gzip");
    }
}

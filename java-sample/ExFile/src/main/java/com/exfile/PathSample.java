package com.exfile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathSample {
    final static Logger logger = LoggerFactory.getLogger(PathSample.class);
    public static void main( String[] args ) throws Exception {
        Path dir = Paths.get("C:\\temp");
        Path filename = Paths.get("foo.dat");

        String path = Paths.get("C:\\temp", "foo.dat").toString();

        // print  C:\temp\foo.dat
        logger.info("{}", path);
    }
}

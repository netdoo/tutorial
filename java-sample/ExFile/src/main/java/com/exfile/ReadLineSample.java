package com.exfile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Created by jhkwon78 on 2017-11-16.
 */
public class ReadLineSample {

    final static Logger logger = LoggerFactory.getLogger(ReadLineSample.class);

    public static void main( String[] args ) throws Exception {

        String fileName = "c:\\temp\\test.txt";

        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            stream.forEach(line -> {
                logger.info("{}", line);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

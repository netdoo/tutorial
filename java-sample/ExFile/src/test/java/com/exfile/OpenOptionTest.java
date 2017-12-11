package com.exfile;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class OpenOptionTest {

    final static Logger logger = LoggerFactory.getLogger(OpenOptionTest.class);

    @Test
    public void testApp() throws Exception {
        String testPath = "./test.txt";

        PrintWriter out = new PrintWriter(Files.newBufferedWriter(Paths.get(testPath), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING));
        out.println("MBC");
        out.println("SBS");
        out.println("KBS");
        out.close();

        List<String> allLines = Files.readAllLines(Paths.get(testPath));
        logger.info("{}", allLines);

        out = new PrintWriter(Files.newBufferedWriter(Paths.get(testPath), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING));
        out.println("RED");
        out.println("GREEN");
        out.println("BLUE");
        out.close();

        allLines = Files.readAllLines(Paths.get(testPath));
        logger.info("{}", allLines);
    }
}

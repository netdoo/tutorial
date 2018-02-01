package com.exresource;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest {

    String fileName = "log4j.properties";
    final static Logger logger = LoggerFactory.getLogger(AppTest.class);

    @BeforeClass
    public static void 테스트_준비() throws Exception {

    }

    @Test
    public void _1_ReadAllLinesTest() throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(classloader.getResourceAsStream(fileName)));) {

            String s = bufferedReader.lines()
                    .collect(Collectors.joining(System.lineSeparator()));

            logger.info("{}", s);

        } catch (Exception e) {
            logger.error("", e);
        }
    }

    @Test
    public void _2_ReadLinesTest() throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(classloader.getResourceAsStream(fileName)));) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                logger.info("{}", line);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
    }
}

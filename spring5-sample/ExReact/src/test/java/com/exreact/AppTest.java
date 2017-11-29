package com.exreact;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

/**
 * Unit test for simple App.
 */
public class AppTest {

    final static Logger logger = LoggerFactory.getLogger(AppTest.class);

    @Test
    public void testApp() throws Exception {
        Files.copy(Paths.get("C:\\temp\\abcd.txt"), Paths.get("C:\\temp\\abcd.copy.txt"), StandardCopyOption.REPLACE_EXISTING);
    }
}

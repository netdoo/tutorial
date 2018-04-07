package com.exfile;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest {

    final static Logger logger = LoggerFactory.getLogger(AppTest.class);

    @Test
    public void testApp() throws Exception {
        Files.copy(Paths.get("C:\\temp\\abcd.txt"), Paths.get("C:\\temp\\abcd.copy.txt"), StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    public void readTest() throws Exception {
        String samplePath = "C:\\Temp\\test.csv";
        List<String> lines = Files.readAllLines(Paths.get(samplePath));
        System.out.println(lines);
    }
}

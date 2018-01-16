package com.exgzip;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

/**
 * Unit test for simple App.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest {

    File source = new File("C:\\temp\\test.png");
    File gzFile = new File("C:\\temp\\test.gz");
    File output = new File("C:\\temp\\test_result.png");

    final static Logger logger = LoggerFactory.getLogger(AppTest.class);

    @Test
    public void _0_테스트_준비() throws Exception {
    }

    @Test
    public void _1_압축_테스트() throws Exception {
        GZip.compress(source, gzFile);
    }

    @Test
    public void _2_압축해제_테스트() throws Exception {
        GZip.decompress(gzFile, output);
    }
}

package com.exfile;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RandomAccessFileTest {

    final static Logger logger = LoggerFactory.getLogger(RandomAccessFileTest.class);

    @Test
    public void testApp() throws Exception {
        String testPath = "./raf.txt";

        RandomAccessFile raf = new RandomAccessFile(testPath, "rw");
        raf.seek(raf.length());
        raf.write("Red".getBytes());
        raf.close();

        raf = new RandomAccessFile(testPath, "rw");
        raf.seek(raf.length());
        raf.write("Green".getBytes());
        raf.close();

        raf = new RandomAccessFile(testPath, "rw");
        raf.seek(0);
        byte readBuffer[] = new byte[32];
        logger.info("[{}], [{}]", new String(readBuffer, 0, raf.read(readBuffer, 0, 3)), raf.getFilePointer());
        logger.info("[{}], [{}]", new String(readBuffer, 0, raf.read(readBuffer, 0, 5)), raf.getFilePointer());
    }
}

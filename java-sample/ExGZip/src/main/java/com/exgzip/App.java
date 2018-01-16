package com.exgzip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class App {
    final static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) throws Exception {
        File source = new File("C:\\temp\\test.png");
        File gzFile = new File("C:\\temp\\test.gz");
        File output = new File("C:\\temp\\test_result.png");
        GZip.compress(source, gzFile);
        GZip.decompress(gzFile, output);
    }
}


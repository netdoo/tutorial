package com.exsqlite;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    final static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args) throws Exception {
//        LocalStorageSample localStorageSample = new LocalStorageSample();
//        localStorageSample.run();
        LocalStorageThreadSample localStorageThreadSample = new LocalStorageThreadSample();
        localStorageThreadSample.runGoodSample();
    }
}

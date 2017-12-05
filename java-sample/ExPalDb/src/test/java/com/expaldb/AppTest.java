package com.expaldb;

import com.linkedin.paldb.api.PalDB;
import com.linkedin.paldb.api.StoreWriter;
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

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest {

    final static Logger logger = LoggerFactory.getLogger(AppTest.class);

    @Test
    public void testApp() throws Exception {
        StoreWriter writer = PalDB.createWriter(new File("./store.paldb"));
        writer.put("001", "mbc");
        writer.put("001", "mbc");
        writer.close();
    }
}

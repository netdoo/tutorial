package com.exxodusdb;

import com.exxodusdb.domain.EPTSVData;
import com.oracle.webservices.internal.api.databinding.DatabindingFactory;
import jetbrains.exodus.ByteIterable;
import jetbrains.exodus.env.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Formatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static jetbrains.exodus.bindings.StringBinding.entryToString;
import static jetbrains.exodus.bindings.StringBinding.stringToEntry;

public class EPSample {
    final static Logger logger = LoggerFactory.getLogger(EPSample.class);
    final static String dbPath = "C:\\Temp\\xodus.db";

    static void print(Environment env, Store store, String title) {
        logger.info("======================== {} =====================", title);
        env.executeInReadonlyTransaction(txn -> {
            try (Cursor cursor = store.openCursor(txn)) {
                while (cursor.getNext()) {
                    logger.info("{} \t\t=> {}", entryToString(cursor.getKey()), entryToString(cursor.getValue()));
                }
            }
        });
        logger.info("===============================================================");
    }

    static void printEP(List<String> epList, String title) {
        logger.info("======================== {} =====================", title);
        epList.forEach(ep -> {
            logger.info("[내보냄] {}", ep);
        });
        logger.info("===============================================================");
    }

    public static void main( String[] args ) throws Exception {

        String readPath = "C:\\temp\\test_all.txt";
        String appendPath = "C:\\temp\\test_all_unique.txt";

        readPath = "C:\\temp\\naver_all.txt";
        appendPath = "C:\\temp\\naver_all_unique.txt";

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        XodusDbRepository xodusDbRepository = new XodusDbRepository();
        xodusDbRepository.open(dbPath, true);
        xodusDbRepository.append(readPath, appendPath);

        xodusDbRepository.close();
        XodusDbRepository.removeDirs(dbPath);

        stopWatch.stop();
        logger.info("elapsed time {} (secs)", stopWatch.getTime(TimeUnit.SECONDS));
    }
}

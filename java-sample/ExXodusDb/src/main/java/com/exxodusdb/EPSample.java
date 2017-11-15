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
        // 기존 embedded db file 지우기
        FileUtils.deleteDirectory(new File(dbPath));



        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        XodusDbRepository xodusDbRepository = new XodusDbRepository();
        xodusDbRepository.removeDirs();
        xodusDbRepository.open(dbPath);
        xodusDbRepository.append("C:\\temp\\test_all.txt", "C:\\temp\\test_all_unique.txt", );

        List<String> allEP = makeAllEp("C:\\RSS\\naver_all.txt", "C:\\RSS\\naver_all_unique.txt", env, store, true);
        //List<String> allEP = makeAllEp("C:\\temp\\test_all.txt", "C:\\temp\\test_all_unique.txt", env, store);




        printEP(allEP, "전체EP");
        print(env, store, "EPDB 상태");
        stopWatch.stop();

        logger.info("elapsed time {} (secs)", stopWatch.getTime(TimeUnit.SECONDS));

        List<String> partEP = makeAllEp("C:\\temp\\test_update.txt", "C:\\temp\\test_update_unique.txt", env, store, false);
        printEP(partEP, "요약EP");
        print(env, store, "EPDB 상태");


        /*
        partEP = makeAllEp("C:\\temp\\tsv_ep_part_201711141021.txt", env, store);
        printEP(partEP, "요약EP");
        print(env, store, "EPDB 상태");
*/
        env.close();
    }
}

package com.exxodusdb;

import jetbrains.exodus.ByteIterable;
import jetbrains.exodus.env.*;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
                    logger.info("key [{}] value [{}]", entryToString(cursor.getKey()), entryToString(cursor.getValue()));
                }
            }
        });
        logger.info("===============================================================");
    }

    static void printEP(List<String> epList, String title) {
        logger.info("======================== {} =====================", title);
        epList.forEach(ep -> {
            logger.info("[요약] {}", ep);
        });
        logger.info("===============================================================");
    }

    static Date parseDate(String dateText) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HH:mm:ss");
        return simpleDateFormat.parse(dateText);
    }

    static String getNamedKey(String[] cols) {
        return cols[1] + "." + cols[2];
    }



    static List<String> makeAllEp(String tsvPath, Environment env, Store store) {
        List<String> targetEPList = new ArrayList<>();

        env.executeInTransaction(txn -> {
            String line, namedKey;
            try (BufferedReader in = Files.newBufferedReader(Paths.get(tsvPath), StandardCharsets.UTF_8)) {
                while ((line=in.readLine()) != null) {
                    try {
                        String cols[] = line.trim().split("\t");
                        namedKey = getNamedKey(cols);
                        Date date = parseDate(cols[3]);

                        // namedkey 중복제거.
                        if (true == store.add(txn, stringToEntry(namedKey), stringToEntry(line))) {
                            targetEPList.add(line);

                            // 시간이 같은 경우 딜 아이디가 적은딜이 먼저나감.
                            // 시간이 다른 경우 판매시작일이 더 빠른딜이 먼저나감.
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return targetEPList;
    }

    public static void main( String[] args ) throws Exception {
        // 기존 embedded db file 지우기
        FileUtils.deleteDirectory(new File(dbPath));
        Environment env = Environments.newInstance(dbPath);

        // Stores can be opened with and without duplicate keys
        Store store = env.computeInTransaction(txn ->
                env.openStore("EP", StoreConfig.WITHOUT_DUPLICATES, txn));

        List<String> allEP = makeAllEp("C:\\temp\\tsv_ep_all.txt", env, store);
        printEP(allEP, "전체EP");
        print(env, store, "ALL_EP_DB");




        List<String> partEP = makeAllEp("C:\\temp\\tsv_ep_part_201711141020.txt", env, store);
        printEP(partEP, "요약EP");
        print(env, store, "PART_EP_DB");

        env.close();
    }
}

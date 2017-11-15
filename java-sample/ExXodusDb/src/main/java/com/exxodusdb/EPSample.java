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

    static boolean isUpsertDeal(Transaction txn, Store store, EPTSVData curr, boolean isAllEP) throws Exception {

        ByteIterable existData = store.get(txn, stringToEntry(curr.getNamedKey()));

        if (null == existData) {
            // 중복된 딜이 없는 경우.
            return true;
        }

        // 중복된 딜이 발견된 경우
        EPTSVData exist = new EPTSVData(entryToString(existData), isAllEP);

        if (isAllEP) {
            // 전체 EP인 경우는 딜 아이디가 적은딜을 내보냄.
            if (Long.valueOf(curr.getId()) < Long.valueOf(exist.getId())) {
                return true;
            }
        } else {
            // 딜 아이디가 적거나 같은딜을 내보냄 - 프리스타일 협의함.
            if (Long.valueOf(curr.getId()) <= Long.valueOf(exist.getId())) {
                return true;
            }
        }



        /*
        if (curr.getTime() == exist.getTime()) {
            // 시간이 같은 경우 딜 아이디가 적은딜을 내보냄.
            if (Long.valueOf(curr.getId()) < Long.valueOf(exist.getId())) {
                return true;
            }
        } else {
            // 시간이 다른 경우 판매시작일이 더 빠른딜을 내보냄.
            if (curr.getTime() < exist.getTime()) {
                return true;
            }
        }
        */

        // 그 외 경우는 무시함.
        return false;
    }

    static List<String> makeAllEp(String tsvPath, String tsvUniquePath, Environment env, Store store, boolean isAllEP) {
        List<String> targetEPList = new ArrayList<>();

        /// append 할 파일이 없으면 생성함.
        try {
            FileUtils.touch(new File(tsvUniquePath));
        } catch (IOException e) {
            logger.error("append path {}", tsvUniquePath, e);
        }

        env.executeInTransaction(txn -> {
            String line;
            int lineCount = 0;
            try (BufferedReader in = Files.newBufferedReader(Paths.get(tsvPath), StandardCharsets.UTF_8);
                 BufferedWriter out = Files.newBufferedWriter(Paths.get(tsvUniquePath), StandardCharsets.UTF_8, StandardOpenOption.APPEND);) {
                while ((line=in.readLine()) != null) {

                    if (lineCount == 0) {
                        // 헤더라인은 무시함.
                        lineCount++;
                        continue;
                    }

                    try {
                        EPTSVData curr = new EPTSVData(line, isAllEP);

                        if (StringUtils.equalsIgnoreCase(curr.getOpClass(), "D")) {
                            // class가 D인 경우는 무조건 내보냄.
                            out.write(line + "\n");
                            //targetEPList.add(line);
                            store.delete(txn, stringToEntry(curr.getNamedKey()));
                            continue;
                        }

                        if (isUpsertDeal(txn, store, curr, isAllEP)) {
                            out.write(line + "\n");
                            //targetEPList.add(line);
                            store.put(txn, stringToEntry(curr.getNamedKey()), stringToEntry(curr.getNamedValue()));
                        }

                        lineCount++;
                        if (lineCount % 10_000 == 0) {
                            logger.info("process {} tsv lines", lineCount);
                        }

                    } catch(Exception e) {
                        logger.error("", e);
                    }
                }
            } catch (Exception e) {
                logger.error("", e);
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

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
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

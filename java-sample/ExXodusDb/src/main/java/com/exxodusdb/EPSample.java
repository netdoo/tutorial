package com.exxodusdb;

import com.exxodusdb.domain.EPFile;
import com.exxodusdb.domain.EPFileLine;
import com.exxodusdb.domain.EPTSVData;
import com.oracle.webservices.internal.api.databinding.DatabindingFactory;
import com.sun.org.apache.xml.internal.dtm.ref.IncrementalSAXSource;
import jetbrains.exodus.ByteIterable;
import jetbrains.exodus.env.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static jetbrains.exodus.bindings.StringBinding.entryToString;
import static jetbrains.exodus.bindings.StringBinding.stringToEntry;

public class EPSample {
    final static Logger LOGGER = LoggerFactory.getLogger(EPSample.class);
    final static String DB_HOME_DIR = "C:\\Temp\\xodus.db";





    static void testEP() throws Exception {
        String readPath = "C:\\temp\\test_all.txt";
         String appendPath = "C:\\temp\\test_all_unique.txt";
        //String readPartPath = "C:\\temp\\test_part.txt";
        // String appendPartPath = "C:\\temp\\test_part_unique.txt";

        readPath = "C:\\RSS\\naver_ep_update_sample.txt";
        readPath = "C:\\temp\\sample.txt";

        makeEP(readPath, appendPath, true);
       // makeEP(readPartPath, appendPartPath, false);
    }

    static void makeEP(String readPath, String appendPath, boolean isAllEP) throws Exception {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        File appendFile = new File(appendPath);

        if (appendFile.exists()) {
            FileUtils.forceDelete(appendFile);
        }

        Files.write(appendFile.toPath(), "id\ttitle\tprice_pc\r\n".getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);

        XodusDbRepository xodusDbRepository = new XodusDbRepository();
        xodusDbRepository.open(DB_HOME_DIR, isAllEP, appendPath);
        xodusDbRepository.append(readPath);
        xodusDbRepository.print();
        xodusDbRepository.close();
        // 3일 이전에 생성된 폴더 삭제.
        XodusDbRepository.removeDirs(DB_HOME_DIR, -3);

        stopWatch.stop();
        LOGGER.info("elapsed time {} (secs)", stopWatch.getTime(TimeUnit.SECONDS));
    }

    static void testRemoveDirs() {
        // 3일 이전에 생성된 폴더 삭제.
        String path = "C:\\Temp\\EP";
        XodusDbRepository.removeDirs(path, -3);
    }

    static void testStartWith() {
        boolean result_1 = StringUtils.startsWithIgnoreCase("liveDealsFile", "liveDealsFile");
        boolean result_2 = StringUtils.startsWithIgnoreCase("liveDealsFileTest", "liveDealsFile");
        LOGGER.info("result1 {} result2 {}", result_1, result_2);
    }

    static void testFileSize() {
        long fileSize = new File("C:\\temp\\out22.csv").length();
        LOGGER.info("size {}", fileSize);
    }

    static void FindDupLine() {
        Path readPath = Paths.get("C:\\Temp\\sample.txt");
        String line, trimLine;
        String namedKey, existValue;
        Map<String, String> ep = new HashMap<>();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(readPath.toFile()), "utf-8"))) {

            while ((line=in.readLine()) != null) {
                trimLine = line.trim();
                if (trimLine.isEmpty())
                    continue;

                if (StringUtils.startsWithIgnoreCase("id\t", trimLine))
                    continue;

                String cols[] = trimLine.split("\t");
                namedKey = cols[1] + "." + cols[2];

                existValue = ep.get(namedKey);
                if (existValue != null) {
                    LOGGER.info("[중복딜] {} {}", namedKey, existValue);
                    continue;
                }

                ep.put(namedKey, cols[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main( String[] args ) throws Exception {
        //testRemoveDirs();
        //testEP();
        //testStartWith();
        //testFileSize();
        //FindDupLine();


    }
}

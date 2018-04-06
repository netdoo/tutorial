package com.exfile;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.input.BOMInputStream;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WriteTest {
    private static final String UTF8_BOM = "\uFEFF";

    @Test
    public void _01_ByteOrderMarkWriteTest() throws Exception {
        Files.write(Paths.get("./FilesWrite.txt"), "한글1A".getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);

        PrintWriter out = new PrintWriter(Files.newBufferedWriter(Paths.get("./PrintWriter.txt"), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE));
        out.println("한글2B");
        out.close();

        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("BufferedWriter.txt"), "UTF-8"));
        bufferedWriter.write(UTF8_BOM);
        bufferedWriter.write("한글3C");
        bufferedWriter.close();
    }

    @Test
    public void _02_ByteOrderMarkRwTest() throws Exception {
        String samplePath = "BOM.txt";
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(samplePath), "UTF-8"));
        bufferedWriter.write(UTF8_BOM);
        bufferedWriter.write("가가가");
        bufferedWriter.write(System.lineSeparator());
        bufferedWriter.write("AAA");
        bufferedWriter.write(System.lineSeparator());
        bufferedWriter.write("111");
        bufferedWriter.close();

        List<String> lines = Files.readAllLines(Paths.get(samplePath));
        System.out.println("#1 Read with BOM Mark. =================");
        System.out.println(lines);
        System.out.println("========================================");

        System.out.println("#2 Read without BOM Mark. ==============");
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new BOMInputStream(new FileInputStream(samplePath), false, ByteOrderMark.UTF_8,
                        ByteOrderMark.UTF_16BE, ByteOrderMark.UTF_16LE,
                        ByteOrderMark.UTF_32BE, ByteOrderMark.UTF_32LE)))) {
            String line;

            while((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            logger.error("파일 읽기 실패 : ", e);
        }

        System.out.println("========================================");

        List<String> lines2 = Files.readAllLines(Paths.get(samplePath));
        if (CollectionUtils.isNotEmpty(lines2)) {
            String line = lines2.get(0);
            if (!line.isEmpty() && line.startsWith(UTF8_BOM)) {
                lines2.set(0, line.substring(1));
            }
        }

        System.out.println("#3 Read without BOM Mark. =================");
        System.out.println(lines2);
        System.out.println("========================================");

    }

    final static Logger logger = LoggerFactory.getLogger(WriteTest.class);
}

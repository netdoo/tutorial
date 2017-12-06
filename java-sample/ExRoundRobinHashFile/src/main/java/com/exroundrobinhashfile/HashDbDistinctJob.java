package com.exroundrobinhashfile;

import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashDbDistinctJob implements Runnable {

    Path readPath;
    final static Logger logger = LoggerFactory.getLogger(HashDbDistinctJob.class);
    HashDbFileLineProcessor hashDbFileLineProcessor;
    public HashDbDistinctJob(Path readPath, HashDbFileLineProcessor hashDbFileLineProcessor) {
        this.readPath = readPath;
        this.hashDbFileLineProcessor = hashDbFileLineProcessor;
    }

    @Override
    public void run() {
        String line;
        Map<String, String> map = new HashMap<>();
        List<String> lines = new ArrayList<>();

        try (BufferedReader in = Files.newBufferedReader(readPath, StandardCharsets.UTF_8);) {
            int pos;
            String key, value;
            while ((line = in.readLine()) != null) {
                Pair<String, String> pair = hashDbFileLineProcessor.onProcess(line);
                map.put(pair.getKey(), pair.getValue());
            }
        } catch (Exception e) {
            logger.error("fail to make unique ", e);
        }

        try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(readPath, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING));) {
            map.forEach((k, v) -> {
                out.println(v);
            });
        } catch (Exception e) {
            logger.error("fail to make result ", e);
        }
    }
}

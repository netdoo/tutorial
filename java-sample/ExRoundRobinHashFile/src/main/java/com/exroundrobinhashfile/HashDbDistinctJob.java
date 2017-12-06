package com.exroundrobinhashfile;

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

    public HashDbDistinctJob(Path readPath) {
        this.readPath = readPath;
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
                pos = line.indexOf("\t");
                key = line.substring(0, pos);
                value = line.substring(pos+1);
                map.put(key, value);
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

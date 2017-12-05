package com.exroundrobinhashfile;

import com.google.code.externalsorting.ExternalSort;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RoundRobinHashFile {
    String roundRobinDir;
    int maxRR;
    boolean isOpen;
    boolean deleteOnExit;
    Map<Integer, Pair<Path, PrintWriter>> printWriters = new HashMap<>();

    final static Logger logger = LoggerFactory.getLogger(RoundRobinHashFile.class);

    public void open(String roundRobinDir, int maxRR, boolean deleteOnExit) throws Exception {
        this.maxRR = maxRR;
        this.roundRobinDir = roundRobinDir;
        this.deleteOnExit = deleteOnExit;

        File dir = new File(this.roundRobinDir);

        if (!dir.exists()) {
            dir.mkdir();
        }

        for (int i = 0; i < maxRR; i++) {
            Path curr = Paths.get(this.roundRobinDir, String.valueOf(i) + ".dat");
            Files.write(curr, "".getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            PrintWriter printWriter = new PrintWriter(Files.newBufferedWriter(curr, StandardCharsets.UTF_8, StandardOpenOption.APPEND));

            Pair<Path, PrintWriter> pair = new ImmutablePair<>(curr, printWriter);
            printWriters.put(Integer.valueOf(i), pair);
        }

        this.isOpen = true;
    }

    public void println(String key, String value) {
        int idx = Math.abs(key.hashCode()) % maxRR;
        Pair<Path, PrintWriter> pair = printWriters.get(idx);
        PrintWriter writer = pair.getValue();
        writer.println(value);
    }

    class MakeUnique implements Runnable {
        Path readPath;
        public MakeUnique(Path readPath) {
            this.readPath = readPath;
        }
        @Override
        public void run() {
            String line;
            Map<String, String> map = new HashMap<>();
            logger.debug("process {}", readPath.toFile().getAbsolutePath());
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

            String resultPath = readPath.toFile().getAbsolutePath() + ".result";

            try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(Paths.get(resultPath), StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW));) {
                map.forEach((k, v) -> {
                    out.println(v);
                });
            } catch (Exception e) {
                logger.error("fail to make result ", e);
            }

            logger.debug("done {}", readPath.toFile().getAbsolutePath());
        }
    }

    public void close() throws Exception {
        this.printWriters.forEach((idx, pair) -> {
            PrintWriter printWriter = pair.getValue();
            printWriter.close();
        });

        ExecutorService p = Executors.newFixedThreadPool(10);

        this.printWriters.forEach((idx, pair) -> {
            Path readPath = pair.getKey();
            p.execute(new MakeUnique(readPath));
        });

        /// shutdown() 메소드 호출전까지 추가된 task 를 처리함.
        p.shutdown();

        /// 추가된 task가 완료될때까지 10분간 기다림.
        p.awaitTermination(10, TimeUnit.MINUTES);

        this.printWriters.clear();
        this.isOpen = false;

        if (deleteOnExit) {
            File dir = new File(this.roundRobinDir);

            if (dir.exists()) {
                Arrays.stream(dir.listFiles()).forEach(File::delete);
            } else {
                dir.mkdir();
            }
        }
    }

    public void sort(Comparator<String> comparator) throws Exception {
        for (int i = 0; i <= maxRR; i++) {
            Path curr = Paths.get(this.roundRobinDir, String.valueOf(i) + ".dat");
            Path sortPath = Paths.get(this.roundRobinDir, String.valueOf(i) + ".sort.dat");
            ExternalSort.mergeSortedFiles(ExternalSort.sortInBatch(curr.toFile(), comparator), sortPath.toFile());
        }
    }
}

package com.exroundrobinhashfile;

import com.google.code.externalsorting.ExternalSort;

import java.io.BufferedWriter;
import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class RoundRobinHashFile {
    String roundRobinDir;
    int maxRR;
    boolean isOpen;
    boolean deleteOnExit;
    List<PrintWriter> printWriters = new ArrayList<>();

    public void open(String roundRobinDir, int maxRR, boolean deleteOnExit) throws Exception {
        this.maxRR = maxRR;
        this.roundRobinDir = roundRobinDir;
        this.deleteOnExit = deleteOnExit;

        File dir = new File(this.roundRobinDir);

        if (!dir.exists()) {
            dir.mkdir();
        }

        for (int i = 0; i <= maxRR; i++) {
            Path curr = Paths.get(this.roundRobinDir, String.valueOf(i) + ".dat");
            Files.write(curr, "".getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            PrintWriter printWriter = new PrintWriter(Files.newBufferedWriter(curr, StandardCharsets.UTF_8, StandardOpenOption.APPEND));
            printWriters.add(printWriter);
        }

        this.isOpen = true;
    }

    public void println(String key, String value) {
        int idx = Math.abs(key.hashCode()) % maxRR;
        PrintWriter writer = printWriters.get(idx);
        writer.println(value);
    }

    public void close() {
        this.printWriters.forEach(printWriter -> {
            printWriter.close();
        });
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

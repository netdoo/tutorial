package com.exfile;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {
    public static boolean removeLine(String path, Pattern removePattern) {

        String outPath = path + ".out";
        String line = null;

        try (BufferedReader in = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8);
             BufferedWriter out = Files.newBufferedWriter(Paths.get(outPath), StandardCharsets.UTF_8)) {

            while ((line=in.readLine()) != null) {
                Matcher m = removePattern.matcher(line);

                if (m != null && m.find()) {
                    continue;
                }

                out.write(line + "\n");
            }
        } catch (IOException e) { return false; }

        try {
            Files.move(Paths.get(outPath), Paths.get(path), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) { return false; }

        return true;
    }

    public static boolean appendLineFile(String readPath, String appendPath) {

        String line = null;

        try (BufferedReader in = Files.newBufferedReader(Paths.get(readPath), StandardCharsets.UTF_8);
             BufferedWriter out = Files.newBufferedWriter(Paths.get(appendPath), StandardCharsets.UTF_8)) {

            while ((line=in.readLine()) != null) {
                line = line.trim();

                if (!line.isEmpty()) {
                    out.write(line + "\n");
                }
            }
        } catch (IOException e) { return false; }

        return true;
    }

    public static boolean appendFile(String readPath, String appendPath) {
        int readSize = 0;
        byte buff[] = new byte[8096];

        /// append 할 파일이 없으면 생성함.
        try {
            FileUtils.touch(new File(appendPath));
        } catch (IOException e) { }

        try (InputStream in =  new BufferedInputStream(Files.newInputStream(Paths.get(readPath)));
             OutputStream out = new BufferedOutputStream(Files.newOutputStream(Paths.get(appendPath), StandardOpenOption.APPEND))) {

            while ((readSize = in.read(buff)) > 0) {
                out.write(buff, 0, readSize);
            }
        } catch (IOException e) { return false; }

        return true;
    }

    /// C:\Temp>fc /b out.txt big.7z
    /// 파일을 비교합니다: out.txt - BIG.7Z
    /// FC: 다른 점이 없습니다.

    public static void main( String[] args ) {
        //removeLine("c://temp//some.json", Pattern.compile("(\"bbsCode\":\"D\")", Pattern.CASE_INSENSITIVE));
        long start = System.currentTimeMillis();
        appendFile("c:\\temp\\big.7z", "c:\\temp\\out.txt");
        appendFile("c:\\temp\\eee.zip", "c:\\temp\\out.txt");
        appendLineFile("c:\\temp\\aaa.txt", "c:\\temp\\out.txt");
        System.out.println("elapsed time : " + (System.currentTimeMillis() - start) + " (ms)");
    }
}


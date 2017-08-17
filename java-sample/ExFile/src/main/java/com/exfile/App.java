package com.exfile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    public static void main( String[] args ) {
        removeLine("c://temp//some.json", Pattern.compile("(\"bbsCode\":\"D\")", Pattern.CASE_INSENSITIVE));
    }
}


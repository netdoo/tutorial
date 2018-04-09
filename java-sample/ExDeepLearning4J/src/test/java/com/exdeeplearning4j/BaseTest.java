package com.exdeeplearning4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class BaseTest {
    public static String getResource(String name) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(BaseTest.class.getResourceAsStream(name)));) {
            return bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (Exception e) {
            return "";
        }
    }
}

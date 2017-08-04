package com.exhash;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class App {
    final static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) {
        List<String> stringList = new ArrayList<>();
        stringList.add("MBC");
        stringList.add("SBS");
        stringList.add("MBC");
        logger.info("stringList {}", stringList);

        Set<String> stringSet = new HashSet<>(stringList);
        List<String> uniqueStringList = new ArrayList<>(stringSet);
        logger.info("uniqueStringList {}", uniqueStringList);
    }
}

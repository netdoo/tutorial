package com.exjava;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ExLambda {
    protected static Logger logger = Logger.getLogger(ExLambda.class);

    public static void main(String[] args) throws Exception {

        normal();
        lambda();
    }


    public static void normal() {
        List<String> list1 = new ArrayList();
        list1.add("normal A");
        list1.add("normal B");
        for (String obj : list1) {
            logger.info("normal : " + obj);
        }
    }

    public static void lambda() {
        List<String> list1 = new ArrayList();
        list1.add("lambda A");
        list1.add("lambda B");
        list1.forEach(obj -> {
            logger.info("lambda : " + obj);
        });
    }
}

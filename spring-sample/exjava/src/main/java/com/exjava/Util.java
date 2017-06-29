package com.exjava;

public class Util {
    public static void sleep(int secs) {
        try{
            Thread.sleep(secs * 1000);
        } catch(Exception e){

        };
    }
}

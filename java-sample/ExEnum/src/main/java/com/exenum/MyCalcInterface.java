package com.exenum;


public interface MyCalcInterface {

    int calc(int x1, int x2);

    default boolean validate(int x1, int x2) {
        return true;
    }
}

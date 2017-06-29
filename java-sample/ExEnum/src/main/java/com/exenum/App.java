package com.exenum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class App {

    final static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) {

        int x1 = 10, x2 = 0;

        /// 모든 경우 조합
        MyCalc[] myCalcs = MyCalc.values();

        List<MyCalc> myEnumList = new ArrayList<>();

        /// 계산이 필요한 경우만 조합
        myEnumList.addAll(Arrays.stream(myCalcs)
                .filter(myEnum -> myEnum.validate(x1, x2))
                .collect(Collectors.toList()));

        List<Integer> resultList = new ArrayList<>();

        /// 계산 결과 조합
        resultList.addAll(myEnumList.stream()
                .map(myEnum -> myEnum.calc(x1, x2))
                .collect(Collectors.toList()));

        System.out.println(resultList);
    }
}

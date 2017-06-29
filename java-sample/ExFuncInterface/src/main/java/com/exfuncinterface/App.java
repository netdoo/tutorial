package com.exfuncinterface;

import com.sun.deploy.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class App {
    final static Logger logger = LoggerFactory.getLogger(App.class);

    /**
     * BiConsumer : 2개의 인자를 받고, 리턴값은 없음.
     */
    static void BiConsumerSample() {
        Map<Integer,String> map = new HashMap<>();

        map.put(1, "Hello");
        map.put(2, "World");
        map.put(3, "2017");

        BiConsumer<Integer,String> biConsumer = (key, value) -> {
            System.out.println("Key:" + key + " Value:" + value);
        };

        map.forEach(biConsumer);
        biConsumer.accept(5, "Hello World 2017");
    }

    /**
     * BiFunction : 2개의 인자를 받아서 Plus 하고, 문자열 결과값을 반환
     */
    static void BiFunctionSample() {
        BiFunction<Integer, Integer, String> Plus  = (num1, num2) -> {
            return Integer.toString(num1 + num2);
        };

        System.out.println(Plus.apply(20,25));
    }

    /**
     * BiPredicate : 2개의 인자를 받고, Bool 값을 반환
     */
    static void BiPredicateSample() {
        BiPredicate<Integer, String> condition = (i, s)-> i>20 && s.startsWith("R");

        System.out.println(condition.test(10,"Mega"));
        System.out.println(condition.test(30,"Radio"));
        System.out.println(condition.test(30,"Remark"));
    }

    static  void CombineListSample() {
        List<String> buckets = new ArrayList<>();
        List<String> results = new ArrayList<>();

        buckets.add("MBC");
        buckets.add("SBS");
        buckets.add("KBS");

        buckets.forEach(bucket -> results.add(bucket) );
        System.out.println(buckets);
    }

    static void CombineStringListSample() {
        String s = StringUtils.join(Arrays.asList("1","2","3").stream().collect(Collectors.toList()), ".");
        String s2 = String.join(".", "1", "2", "3");

        System.out.println(s);
        System.out.println(s2);
    }

    public static void main( String[] args ) {
        /// 동적으로 로그레밸 설정
        org.apache.log4j.Logger logger4j = org.apache.log4j.Logger.getRootLogger();
        logger4j.setLevel(org.apache.log4j.Level.toLevel("INFO"));

        BiConsumerSample();
        BiFunctionSample();
        BiPredicateSample();
        CombineListSample();
        CombineStringListSample();
    }
}


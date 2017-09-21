package com.exjava;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LimitSample {
    public static void main( String[] args ) {
        List<Integer> numbers = new ArrayList(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        List<Integer> result = numbers.stream()
                .filter(n -> {
                    System.out.println("filter : " + n);
                    return (n > 2);
                    })
                .limit(5)
                .collect(Collectors.toList());

        System.out.println("실행결과 : " + result);
    }
}

package com.exjava;

import java.util.*;
import java.util.stream.Collectors;

public class ToMapSample {

    static class Box {
        String code;
        String color;

        public Box(String code, String color) {
            this.code = code;
            this.color = color;
        }

        public String getCode() {
            return this.code;
        }

        public String getColor() {
            return this.color;
        }

        @Override
        public String toString() {
            return "Box {" +
                    "code=" + this.code +
                    ", color=" + this.color +
                    "}";
        }
    }

    public static void main(String[] args) {
        List<Box> boxes = Arrays.asList(
                new Box("1", "red"),
                new Box("2", "green"),
                new Box("3", "blue"),
                new Box("3", "darkblue")
        );

        Map<String, Box> boxMap = boxes
                .stream()
                .collect(Collectors.toMap(Box::getCode,          // key mapper
                                          box -> box,            // value mapper
                                          (existBox, newBox) -> {   // merge function
                                              // 이미 키값이 존재하는 경우,
                                              // 이곳에서 어떻게 할지 결정함.
                                              return newBox;
                                          }));

        boxMap.forEach((k, v) -> {
            System.out.println(v);
        });
    }
}

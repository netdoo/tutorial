package com.exjava;

import java.util.ArrayList;
import java.util.List;

public class App {
    static class Toy {
        String name;
        int price;

        public Toy(String name, int price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getPrice() {
            return this.price;
        }

        public void setPrice(int price) {
            this.price = price;
        }
    }

    public static void main( String[] args ) {

        List<Toy> toys = new ArrayList<>();

        toys.add(new Toy("car", 5000));
        toys.add(new Toy("robot", 20000));
        toys.add(new Toy("ball", 10000));
        toys.add(new Toy("pen", 3000));

        int toyPrice = toys.stream()
                .filter(toy -> {
                    /// 전체 스트림에서 개별요소를 걸러내기 위한 필터
                    if (toy.getPrice() > 5000) {
                        return true;
                    }
                    return false;
                }).map(toy -> {
                    /// 필터된 개별요소에 적용할 연산
                    return toy.getPrice();
                }).reduce(0, Integer::sum); /// 스트림을 단일 요소로 반환

        System.out.println("price : " + toyPrice);
    }
}

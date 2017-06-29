package com.exinterface;

public class App {
    /**
     * Java 8 부터는 interface 안에서도 default 키워드를 사용하면
     * 함수 구현이 가능해졌다.
     */
    interface Animal {
        String name();
    }

    /**
     * interface 안에서도 default 키워드를 사용하면 함수 구현이 가능하다.
     */
    interface Cat extends Animal {
        @Override
        default String name() {
            return "cat";
        }
    }

    interface Rabbit extends Animal {
        @Override
        default String name() {
            return "rabbit";
        }
    }

    /**
     * 다중상속 예제
     */
    class Zoo implements Cat, Rabbit {
        @Override
        public String name() {
            /// super 키워드를 사용하여, 상속받은 객체에 접근할 수 있다.
            return Cat.super.name() +
                    ", " +
                    Rabbit.super.name();
        }
    }

    public void Test() {
        Zoo zoo = new Zoo();
        System.out.println(zoo.name());
    }

    public static void main( String[] args ) {
        App app = new App();
        app.Test();
    }
}

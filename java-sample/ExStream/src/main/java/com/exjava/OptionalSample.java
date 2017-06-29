package com.exjava;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class OptionalSample {

    static class Car {
        // 빈 Optional 객체 반환
        Optional<Audio> audio = Optional.empty();

        public void setAudio(Optional<Audio> audio) {
            this.audio = audio;
        }

        public Optional<Audio> getAudio() {
            return this.audio;
        }
    }

    static class Audio {
        String name;

        public Audio(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static void caseEmpty() {
        /// String이 null이면 빈 Optional 객체 반환
        Optional<String> empty = Optional.ofNullable(null);

        /// 내부 객체를 반환한다. 내부 객체가 null이면 인자로 들어간 기본값을 반환한다.
        String result = empty.map(String::toString).orElse("Default Value");

        System.out.println("[caseEmpty] result : " + result);
    }

    public static void caseHave() {
        /// String이 null이 아니기 때문에, "Hello Optional" String 객체 반환.
        Optional<String> have = Optional.ofNullable("Hello Optional");
        String result = have.map(String::toUpperCase).orElse("Default Value");

        System.out.println("[caseHave] result : " + result);
    }

    public static void caseObject() {
        Car car = new Car();
        System.out.println("[caseObject] #1 car audio name : " + car.getAudio().map(Audio::getName).orElse("no audio").toString());

        car.setAudio(Optional.ofNullable(new Audio(null)));
        System.out.println("[caseObject] #2 car audio name : " + car.getAudio().map(Audio::getName).orElse("no audio").toString());

        car.setAudio(Optional.ofNullable(new Audio("SONY")));
        System.out.println("[caseObject] #3 car audio name : " + car.getAudio().map(Audio::getName).orElse("no audio").toString());
    }

    public static void caseFindOptional() {
        List<String> studio = Arrays.asList("MBC", "SBS", "kbs", "KBS");
        String result1 = studio.stream().filter(name -> name.equalsIgnoreCase("TVN")).findFirst().orElse("");
        String result2 = studio.stream().filter(name -> name.equalsIgnoreCase("KBS")).findFirst().orElse("");

        System.out.println("[caseFindOptional] result1 : " + result1);
        System.out.println("[caseFindOptional] result2 : " + result2);
    }

    public static void main( String[] args ) {
        caseEmpty();
        caseHave();
        caseObject();
        caseFindOptional();
    }
}

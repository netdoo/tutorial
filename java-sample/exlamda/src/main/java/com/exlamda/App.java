package com.exlamda;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    final static Logger logger = LoggerFactory.getLogger(App.class);

    @FunctionalInterface
    interface TextHelper {
        /// 람다식을 위한 추상메소드는 반드시 한개이어야 함.
        /// @FunctionalInterface 추가시, 컴파일 타임에서 메소드가 여러개 선언된 경우 오류 발생.
        public String convert(String in);
    }

    public static void main( String[] args ) {
        String sampleText = " Hello World ";

        /// Java 8 이전
        TextHelper trim = new TextHelper() {
            @Override
            public String convert(String in) {
                return in.trim();
            }
        };

        /// Java 8 이후 람다식 적용
        /// (파라미터) -> (구현부)
        TextHelper trim8 = (String s) -> {
            return s.trim();
        };

        TextHelper upper = (String s) -> s.toUpperCase();
        TextHelper lower = String::toLowerCase;

        logger.info("trim [{}] => [{}]",  sampleText, trim.convert(sampleText));
        logger.info("trim8 [{}] => [{}]",  sampleText, trim8.convert(sampleText));
        logger.info("upper [{}] => [{}]",  sampleText, upper.convert(sampleText));
        logger.info("lower [{}] => [{}]",  sampleText, lower.convert(sampleText));
    }
}

package com.exre;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class HanTest {

    final Logger logger = LoggerFactory.getLogger(HanTest.class);

    /*
    유니코드에서 한글을 표현하는 방법

    유니코드 범위 목록(Mapping of Unit characters)을 살펴보면,
    한글 표현을 위한 코드 영역 개수는 다른 언어 글자를 위한 코드 영역 개수보다 대체로 많다는 것을 알 수 있다.
    유니코드에서 한글을 표현하기 위한 코드 영역은 다음과 같다.


    표2 유니코드 범위 목록에서의 한글 관련 범위

    이름	                                        처음	    끝	    개수
    ==============================================  ====       =====    ====
    한글 자모 (Hangul Jamo)	                        1100	    11FF	256
    호환용 한글 자모 (Hangul Compatibility Jamo)	3130	    318F	96
    한글 자모 확장 A (Hangul Jamo Extended A)	    A960	    A97F	32
    한글 소리 마디 (Hangul Syllables)	            AC00	    D7AF	11184
    한글 자모 확장 B (Hangul Jamo Extended B)	    D7B0	    D7FF	80
     */
    static boolean isHan(char c) {
        if (c >= 0x1100 && c <= 0x11FF) {
            // 한글 자모 (Hangul Jamo)
            return true;
        } else if (c >= 0x3130 && c <= 0x318F) {
            // 호환용 한글 자모(Hangul Compatibility Jamo)
            return true;
        } else if (c >= 0xA960 && c <= 0xA97F) {
            // 한글 자모 확장 A (Hangul Jamo Extended A)
            return true;
        } else if (c >= 0xAC00 && c <= 0xD7AF) {
            // 한글 소리 마디(Hangul Syllables)
            return true;
        } else if (c >= 0xD7B0 && c <= 0xD7FF) {
            // 한글 자모 확장 B (Hangul Jamo Extended B)
            return true;
        }

        return false;
    }

    static boolean isHan(String text) {
        if (text == null)
            return false;

        for (char c : text.toCharArray()) {
            if (!isHan(c))
                return false;
        }

        return true;
    }

    @Test
    public void testIsHan() {
        logger.info("{}", isHan('한'));
        logger.info("{}", isHan("한글"));
        logger.info("{}", isHan('꾘'));
        logger.info("{}", isHan('A'));
        logger.info("{}", isHan('5'));
        logger.info("{}", isHan('#'));

        String input = "블루L";
        StringBuilder stringBuilder = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (isHan(c)) {
                stringBuilder.append(c);
            } else {
                break;
            }
        }

        String left = stringBuilder.toString();
        String right = input.substring(left.length());

        logger.info("left {} right {}", left, right);
    }
}

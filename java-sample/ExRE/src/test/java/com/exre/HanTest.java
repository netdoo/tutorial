package com.exre;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class HanTest {

    final Logger logger = LoggerFactory.getLogger(HanTest.class);

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

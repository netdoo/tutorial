package com.exre;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CharTypeTest {

    final Logger logger = LoggerFactory.getLogger(CharTypeTest.class);

    @Test
    public void testCharType() {

        List<String> textList = new ArrayList<>();

        textList.add("한");
        textList.add("글");
        textList.add("A");
        textList.add("b");
        textList.add("C");
        textList.add("0");
        textList.add("7");

        textList.forEach(text -> {
            logger.info("{} => {}", text, CharType.analyze(text));
        });
    }
}

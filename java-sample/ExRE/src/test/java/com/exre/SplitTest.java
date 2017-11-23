package com.exre;


import com.google.re2j.Pattern;
import org.apache.logging.log4j.util.Strings;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class SplitTest {

    final Logger logger = LoggerFactory.getLogger(SplitTest.class);

    Set<String> loadPattern(String path) {
        Set<String> pattern = new HashSet<>();
        try {
            Files.lines(Paths.get(path)).forEach(line -> {
                pattern.add(line.toUpperCase());
            });
        } catch (Exception e) {

        }
        return pattern;
    }


    List<String> subSplit(String text) {

        if (text == null || text.isEmpty())
            return new ArrayList<>();

        char c = 0;
        CharType findType;
        List<String> terms = new ArrayList<>();
        StringBuilder termBuffer = new StringBuilder();

        // 한글 + [영어,숫자]인 경우만 처리됨.
        // 예1) 블루L => 블루, L
        // 예2) L블루 => L,블루
        for (int i = 0; i < text.length(); i++) {
            for (findType = CharType.analyze(text.charAt(i)); i < text.length(); i++) {
                c = text.charAt(i);
                if (CharType.analyze(c) == findType) {
                    termBuffer.append(c);
                } else {
                    if (termBuffer.length() > 0) {
                        terms.add(termBuffer.toString());
                    }
                    termBuffer.setLength(0);
                    i--;
                    break;
                }
            }
        }

        if (termBuffer.length() > 0) {
            terms.add(termBuffer.toString());
        }

        return terms;
    }

    String replacePattern(String input) {
        String trimPattern = "[\\(,\\[,\\],\\)]";
        Set<String> colorPattern = loadPattern("C:\\Temp\\colorPattern.txt");
        Set<String> sizePattern = loadPattern("C:\\Temp\\sizePattern.txt");
        List<String> termList = new ArrayList<>();

        // 1. 공백단위 Term 분리
        String terms[] = input.split(" ");

        for (String term : terms) {

            // 2. Term에서 노이즈 제거
            String trimTerm = term.replaceAll(trimPattern, "");
            String trimTermUpperCase = trimTerm.toUpperCase();

            // 3. Term이 Color 또는 Size 패턴인지 비교함.
            if (colorPattern.contains(trimTermUpperCase)) {
                // 컬러 또는
                termList.add("#color#");
                continue;
            } else if (sizePattern.contains(trimTerm.toUpperCase())) {
                // 사이즈인 경우는 특수문자로 치환함.
                termList.add("#size#");
                continue;
            }

            // 4. 한개의 Term안에 색상, 사이즈가 혼합된 경우, 각각 색상, 사이즈 Term 단위로 분리
            List<String> subTerms = subSplit(trimTerm);

            // 5. 최종 분리된 Term 을 기준으로, 부호화 함.
            subTerms.forEach(subTerm -> {
                String subTermUpperCase = subTerm.toUpperCase();
                if (colorPattern.contains(subTermUpperCase)) {
                    // 컬러 또는
                    termList.add("#color#");
                } else if (sizePattern.contains(subTermUpperCase)) {
                    // 사이즈인 경우는 특수문자로 치환함.
                    termList.add("#size#");
                } else {
                    termList.add(subTerm);
                }
            });
        }

        // 6. 부호화된 Term을 하나로 결합하여 중복여부를 판별할 Key 값으로 생성함.
        return Strings.join(termList, '.');
    }

    @Test
    public void testSubSplit() {
        List<String> textList = new ArrayList<>();
        textList.add("블루L");
        textList.add("L블루");
        textList.add("L블루100");
        textList.add("100L블루");

        textList.forEach(text -> {
            logger.info("{} => {}", text, subSplit(text));
        });
    }

    @Test
    public void testReplace() {

        List<String> inputList = new ArrayList<>();

        inputList.add("나이키 운동화 블루 260mm");
        inputList.add("나이키 운동화 Blue 260mm");
        inputList.add("나이키 운동화 파란색 270mm");
        inputList.add("도그포즈 애견가운 (블루L)");
        inputList.add("도그포즈 애견가운 (블루M)");
        inputList.add("도그포즈 애견가운 (블루XL)");

        inputList.forEach(input -> {
            logger.info("{} => {}", input, replacePattern(input));
        });
    }
}

package com.exxodusdb;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by jhkwon78 on 2017-11-17.
 */
public class TermTest {

    Map<String, String> colorDict = new HashMap<>();

    public TermTest() {
        // 색상 부호 테이블 로드
        colorDict.put("화이트", "Color001");
        colorDict.put("WHITE", "Color001");
        colorDict.put("백색", "Color001");
        colorDict.put("흰색", "Color001");
    }

    Set<String> convertTermsSet(String text) {
        String terms[] = text.split(" ");
        Set<String> termSec = new HashSet<>();

        for (String term : terms) {
            String colorCode = colorDict.get(term.toUpperCase());
            if (colorCode != null) {
                termSec.add(colorCode);
            } else {
                termSec.add(term);
            }
        }

        return termSec;
    }

    @Test
    public void testEqual() {

        // 아디다스 운동화         (o) 공백단위 기준의 색상만 분류함.
        // 아디다스_운동화         (x) 노이즈 제거를 위한 전처리 과정
        // 아디다스운동화          (x) 형태소 분석기 필요함.
        // 아디타스운동화          (x) 형태소 분석기 필요함. (오타교정)

        // 1. 색상을 부호로 치환
        Set<String> termSec1 = convertTermsSet("아디다스 화이트 운동화");
        Set<String> termSec2 = convertTermsSet("White 아디다스 운동화");
        Set<String> termSec3 = convertTermsSet("백색 아디다스 운동화");

        // 2. 문자열을 TERM 단위로 분리후, 정렬후, 결합
        String encTitle1 = StringUtils.join(termSec1, ".");
        String encTitle2 = StringUtils.join(termSec2, ".");
        String encTitle3 = StringUtils.join(termSec3, ".");

        // 3. 부호화되고 정렬된 문자열을 비교함.
        Assert.assertTrue(StringUtils.equals(encTitle1, encTitle2));
        Assert.assertTrue(StringUtils.equals(encTitle2, encTitle3));
    }
}

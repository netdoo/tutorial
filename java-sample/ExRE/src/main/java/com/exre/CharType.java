package com.exre;

/**
 * Created by jhkwon78 on 2017-11-23.
 */
public enum CharType {
    UNKNOWN(0),
    NUM(1),
    ENG(2),
    HAN(3);

    final int id;

    CharType(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

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
    public static boolean isHan(char c) {
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


    public static boolean isHan(String text) {
        if (text == null)
            return false;

        for (char c : text.toCharArray()) {
            if (!isHan(c))
                return false;
        }

        return true;
    }


    public static boolean isEng(char c) {
        if (c >= 'a' && c <= 'z') {
            return true;
        } else if (c >= 'A' && c <= 'Z') {
            return true;
        }

        return false;
    }

    public static boolean isEng(String text) {
        if (text == null)
            return false;

        for (char c : text.toCharArray()) {
            if (!isEng(c))
                return false;
        }

        return true;
    }

    public static boolean isNum(char c) {
        if (c >= '0' && c <= '9') {
            return true;
        }

        return false;
    }

    public static boolean isNum(String text) {
        if (text == null)
            return false;

        for (char c : text.toCharArray()) {
            if (!isNum(c))
                return false;
        }

        return true;
    }

    public static CharType analyze(char c) {
        return analyze(String.valueOf(c));
    }

    public static CharType analyze(String text) {
        if (isHan(text)) {
            return CharType.HAN;
        } else if (isEng(text)) {
            return CharType.ENG;
        } else if (isNum(text)) {
            return CharType.NUM;
        }

        return CharType.UNKNOWN;
    }
}

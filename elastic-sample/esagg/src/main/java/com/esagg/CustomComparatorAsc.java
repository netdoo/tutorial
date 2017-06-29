package com.esagg;

import java.util.Comparator;

// 오름차순
class CustomComparatorAsc implements Comparator<String> {

    private static final int REVERSE  = -1;
    private static final int LEFT_FIRST  = -1;
    private static final int RIGHT_FIRST  = 1;

    enum TextType {
        Korean,
        English,
        Number
    }

    public static final CustomComparatorAsc TERM_ASC = new CustomComparatorAsc();

    public static TextType getTextType(String s) {
        if (s.chars().anyMatch(c -> Character.getType(c) == Character.OTHER_LETTER)) {
            return TextType.Korean;
        }

        if (s.chars().allMatch(Character::isDigit)) {
            return TextType.Number;
        }

        return TextType.English;
    }

    @Override
    public int compare(String o1, String o2) {
        /// 영어 한글 숫자 순
        TextType leftType = getTextType(o1);
        TextType rightType = getTextType(o2);

        if (leftType == TextType.Korean) {
            switch (rightType) {
                case English:
                    return RIGHT_FIRST;
                case Number:
                    return LEFT_FIRST;
                case Korean:
                    return o1.compareTo(o2);
            }
        } else if (leftType == TextType.English) {
            switch (rightType) {
                case Korean:
                case Number:
                    return  LEFT_FIRST;
                case English:
                    return o1.compareTo(o2);
            }
        } else if (leftType == TextType.Number) {
            switch (rightType) {
                case Korean:
                case English:
                    return  RIGHT_FIRST;
                case Number:
                    return o1.compareTo(o2);
            }
        }

        return o1.compareTo(o2);
    }
}
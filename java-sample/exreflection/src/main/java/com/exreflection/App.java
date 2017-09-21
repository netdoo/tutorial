package com.exreflection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class App {

    final static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) throws Exception {
        Class calcClass = Class.forName("com.exreflection.Calc");
        Object calc = calcClass.newInstance();

        Method plus = calcClass.getDeclaredMethod("plus", new Class[]{Integer.class, Integer.class});
        Method minus = calcClass.getDeclaredMethod("minus", new Class[]{Integer.class, Integer.class});
        Method multiply = calcClass.getDeclaredMethod("multiply", new Class[]{Integer.class, Integer.class});

        /// public, protected 메소드를 제외한
        /// private method 에 접근하기 위해서 사용함.
        minus.setAccessible(true);

        System.out.println("Plus : " + (Integer)plus.invoke(calc, new Object[]{1, 2}));
        System.out.println("Minus : " + (Integer)minus.invoke(calc, new Object[]{10, 2}));
        System.out.println("Multiply : " + (Integer)multiply.invoke(calc, new Object[]{10, 2}));
    }
}

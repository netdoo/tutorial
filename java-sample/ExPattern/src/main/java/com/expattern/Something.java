package com.expattern;

public class Something {
    private Something() {
        System.out.println("Create Something Class");
    }

    private static class LazyHolder {
        private static final Something INSTANCE = new Something();
    }

    public static Something getInstance() {
        return LazyHolder.INSTANCE;
    }
}


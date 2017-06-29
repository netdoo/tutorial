package com.expattern;

public class App {
    public void singleton() {
        Something a = Something.getInstance();
        Something b = Something.getInstance();

        System.out.println("a : " + a.hashCode());
        System.out.println("b : " + b.hashCode());
    }

    public static void main( String[] args ) {
        App app = new App();
        app.singleton();
    }
}

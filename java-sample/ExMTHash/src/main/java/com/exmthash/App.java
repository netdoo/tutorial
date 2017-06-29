package com.exmthash;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class App {

    final int MAX_THREAD = 10;
    final int MAX_LOOP_COUNT = 900000;

    static HashMap<String, Integer> hashMap = new HashMap<>();
    static ConcurrentHashMap<String, Integer> concurrentHashMap = new ConcurrentHashMap<>();

    public void multiThread() throws Exception {

        long start = System.nanoTime();

        ExecutorService es = Executors.newFixedThreadPool(MAX_THREAD);
        for (int i = 0; i < MAX_THREAD; i++) {
            es.execute(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < MAX_LOOP_COUNT; i++) {
                        String key = String.valueOf(i) + "0";
                        key = key + "0";
                        hashMap.put(key, i);
                        concurrentHashMap.put(key, i);
                    }
                }
            });
        }

        es.shutdown();
        es.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

        long elapsed = System.nanoTime() - start;

        System.out.println("HashMap Size : " + hashMap.size());
        System.out.println("ConcurrentHashMap Size : " + concurrentHashMap.size());
        System.out.println("Elapsed " + elapsed + " (ns)");
    }

    public void parallel() throws Exception {

        hashMap.clear();
        concurrentHashMap.clear();

        long start = System.nanoTime();

        IntStream.range(0, MAX_THREAD).parallel().forEach(index -> {
            for (int i = 0; i < MAX_LOOP_COUNT; i++) {
                String key = String.valueOf(i) + "0";
                key = key + "0";
                hashMap.put(key, i);
                concurrentHashMap.put(key, i);
            }
        });

        long elapsed = System.nanoTime() - start;

        System.out.println("Parallel HashMap Size : " + hashMap.size());
        System.out.println("Parallel ConcurrentHashMap Size : " + concurrentHashMap.size());
        System.out.println("Parallel Elapsed " + elapsed + " (ns)");
    }

    public static void main( String[] args ) throws Exception {
        App app = new App();
        app.multiThread();
        app.parallel();
    }
}

package com.exjava;


import org.apache.log4j.Logger;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.*;

/**
 * 1. CompletableFuture - 완전한 비동기식 병렬 프로그래밍

 이번 글에서는 람다기반 비동기식 병렬 프로그래밍 기법인 CompletableFuture<T>에 대해서 알아보자.  CompletableFuture의 사전적 의미는 다음과 같다.

 "미래에 처리할 업무(Task)로서,  Task 결과가 완료되었을때 값을 리턴하거나, 다른 Task가 실행되도록 발화(trigger)시키는 Task."

 CompletableFuture는 자바 비동기식 프로그래밍의 끝판왕이라 복잡한면도 있지만. 부담 갖지 말자. 이 글에선 가장 기초적인 부분과 쉬운 예제에 집중할 것이다. 또한 여기 나오는 모든 예제는 즉시 실행가능하거나, 약간의 예외 처리만 추가해주면 가능할 것이므로, 설명이 와 닿지 않을 경우 직접 실행해보면 좋을것 같다.


 2. CompletableFuture의 장점

 1) 명시적 쓰레드 선언없이 쓰레드를 사용할수 있다.
 2) 함수형 프로그래밍방식으로 비동기적으로 동시성/병렬 프로그래밍을 가능하게 함으로서 의도를 명확하게 드러내는 함축적인 프로그래밍을 가능하게 한다.
 3) 각 타스크마다 순서적 연결을 할수도 있고, 타스크의 예외처리도 가능하다.

 */

public class ExCompletableFuture {
    protected static Logger logger = Logger.getLogger(ExCompletableFuture.class);

    public static void runAsync() throws Exception {

        long startTime = System.currentTimeMillis();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        CompletableFuture completableFuture = CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                Util.sleep(5);
                logger.info("hello");

            }
        }, executorService).thenRun(new Runnable() {

            /// 앞에 등록된 작업들이 완료된 이후 호출됨.
            @Override
            public void run() {
                Util.sleep(5);
                logger.info("world");

            }
        });

        completableFuture.join();
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        logger.info("elapsed time " + (System.currentTimeMillis() - startTime) + " (milli)");
    }

    /**
     * 7.1 Pattern 1 -  연속적 연결

     하나의 타스크의 완료 혹은 예외 상황을 다른 타스크와 연결시키는 것.
     * @throws Exception
     */
    public static void thenApply() throws Exception {
        long startTime = System.currentTimeMillis();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        CompletableFuture cf= CompletableFuture.supplyAsync(new Supplier<String>() {

            @Override
            public String get() {
                return "hello world";
            }
        }, executorService).thenApply(new Function<String, Object>() {

            @Override
            public String apply(String s) {
                return s + "2017";
            }

        }).thenAccept(new Consumer<Object>() {
            @Override
            public void accept(Object o) {
                String s = (String)o;
                logger.info(Thread.currentThread().getName() + " : " + s);
            }
        });

        cf.join();
        String result = (String) cf.get();
        logger.info(Thread.currentThread().getName() + " : " + result);
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
        logger.info("elapsed time " + (System.currentTimeMillis() - startTime) + " (milli)");
    }

    /**
     * 7.2 Pattern 2 - 타스크 간의 조합
     : ComletableFuture 의 수행중 완전히 다른 CompletableFuture 를 조합하여 실행할수 있다.
     * @throws Exception
     */
    public static void composeAsync() throws Exception {
        long startTime = System.currentTimeMillis();
        ExecutorService executorService = Executors.newCachedThreadPool();
        CompletableFuture cf1 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                logger.info(Thread.currentThread().getName() + " start");
                Util.sleep(3);
                logger.info(Thread.currentThread().getName() + " done");
                return 100;
            }
        });

        CompletableFuture cf2 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                logger.info(Thread.currentThread().getName() + " start");
                Util.sleep(1);
                logger.info(Thread.currentThread().getName() + " done");
                return 200;
            }
        });

        CompletableFuture cf3 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                logger.info(Thread.currentThread().getName() + " start");
                logger.info(Thread.currentThread().getName() + " done");
                return 300;
            }
        }, executorService);

        cf3.thenComposeAsync(new Function<Integer, CompletionStage<Integer>>() {
            @Override
            public CompletionStage<Integer> apply(Integer integer) {
                return cf2;
            }
        }).thenComposeAsync(new Function<Integer, CompletionStage<Integer>>() {
            @Override
            public CompletionStage<Integer> apply(Integer integer) {
                return cf1;
            }
        });

        logger.info(Thread.currentThread().getName() + " wait");
        cf3.join();
        logger.info(Thread.currentThread().getName() + " done");

        logger.info("final cf1.get() = " + cf1.get()+ " cf2.get()="+cf2.get()+" cf3.get()="+cf3.get()+" now="+(System.currentTimeMillis()-startTime));
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

        logger.info(Thread.currentThread().getName() + " finish");
    }

    /**
     *
     7.3 Pattern 3 -  두 타스크들 결과를 모두 사용한 타스크간 결합
     : 두 타스크의 결과를 모두 기다렸다가 결과들을 조합하여 그다음 일을 하는것)
     * @throws Exception
     */
    public static void combineAsync() throws Exception {
        /**
         * 앞의 타스크와 파라미터로 받은 타스크의 결과를 입력값으로 받아서 새로운 결과를 리턴한다. get으로서 결과를 얻을수 있다.
         */
        ExecutorService executorService = Executors.newCachedThreadPool();
        CompletableFuture cf1 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                Util.sleep(3);
                return 100;
            }
        });

        CompletableFuture cf2 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                Util.sleep(1);
                return 200;
            }
        }, executorService).thenCombineAsync(cf1, new BiFunction() {
            @Override
            public Object apply(Object o, Object o2) {
                Integer x = (Integer)o;
                Integer y = (Integer)o2;
                Integer ret = x + y;
                return (Object)ret;
            }
        });

        logger.info("result : " + cf2.get());
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

        logger.info(Thread.currentThread().getName() + " finish");
    }

    public static void acceptBoth() throws Exception {
        // 앞의 타스크의 결과, 파라미터로 받은 타스크의 결과를 받아 리턴은 하지않고 결과를 소비하는 패턴이다.
        ExecutorService executorService = Executors.newCachedThreadPool();
        CompletableFuture cf1 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                Util.sleep(3);
                return 100;
            }
        });
        CompletableFuture cf2 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                Util.sleep(1);
                return 200;
            }
        }, executorService).thenAcceptBothAsync(cf1, new BiConsumer() {
            @Override
            public void accept(Object o, Object o2) {
                Integer x = (Integer)o;
                Integer y = (Integer)o2;
                logger.info("x="+x+" y="+y);
            }
        });

        cf2.join();

        logger.info("final result = " + cf2.get());
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

        logger.info(Thread.currentThread().getName() + " finish");
    }

    /**
     * 7.4  Pattern 4 - 먼저 도착한 타스크 결과를 사용하는 타스크 결합방법
     : 두 타스크중에 먼저 결과를 내는 쪽의 결과값을 가지고 타스크를 실행한다.
     * @throws Exception
     */
    public static void applyToEither() throws Exception {
        /**
         * 앞의 타스크와 파라미터로 받은 타스크중에 더 빨리 결과를 리턴하는 타스크의 결과를 get을 통해 얻을수 있다.
         appyToEither를 연속적으로 써서 여러 타스크들을 연결할수 있다.
         */
        ExecutorService executorService = Executors.newCachedThreadPool();
        CompletableFuture cf1 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                Util.sleep(5);
                return 100;
            }
        });


        CompletableFuture cf2 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                Util.sleep(1);
                return 200;
            }
        });

        CompletableFuture cf3 = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                Util.sleep(3);
                return 300;
            }
        }, executorService).applyToEither(cf1, new Function<Integer /*input*/, Object /*return*/>() {
            @Override
            public Object apply(Integer s) {
                logger.info("cf1 is done");
                return s;
            }
        }).applyToEither(cf2, new Function<Integer, Object>() {
            @Override
            public Object apply(Integer o) {
                logger.info("cf2 is done");
                return o;
            }
        });

        logger.info("final result = " + cf3.get());
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

        logger.info(Thread.currentThread().getName() + " finish");
    }
}

package com.exjava;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public class ExThreadPool {
    protected static Logger logger = Logger.getLogger(ExThreadPool.class);
    public static volatile boolean done = false;

    /// 1개의 스레드풀로 작업을 실행하고, 결과값 확인은 불가능함.
    public static void singleRunnable() {

        done = false;
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Util.sleep(5);
                logger.info(Thread.currentThread().getName() + "Hello!");
            }
        });

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Util.sleep(1);
                logger.info(Thread.currentThread().getName() + "World ");
            }
        });

        executor.shutdown();

        try {
            executor.awaitTermination(10, TimeUnit.SECONDS);
            executor.shutdownNow();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        done = true;
    }

    /// 1개의 스레드풀로 작업을 실행하고, 결과값을 Future 객체를 통해서 확인함.
    public static void singleCallable() {
        done = false;
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        final Collection<Callable<String>> jobs = new ArrayList<Callable<String>>();

        jobs.add(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Util.sleep(3);
                logger.info(Thread.currentThread().getName() + "hello ");
                return "hello ";
            }
        });

        jobs.add(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Util.sleep(1);
                logger.info(Thread.currentThread().getName() + "world");
                return "world ";
            }
        });

        try {
            List<Future> futureList =  executorService.invokeAll((Collection)jobs);
            for(Future future : futureList ) {
                /// Future::get 메소드는 해당 스레드가 종료될때까지 블록킹 됨.
                logger.info(Thread.currentThread().getName() + " : " + future.get());
            }

            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException  e) {
            e.printStackTrace();
        }

        done = true;
    }

    /// 5개의 스레드풀로 작업을 실행하고, 결과값을 Future 객체를 통해서 확인함.
    /// 본 예제에서는 world가 먼저 출력이 된다.
    public static void invokeAll() {
        done = false;
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        final Collection<Callable<String>> jobs = new ArrayList<Callable<String>>();

        jobs.add(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Util.sleep(3);
                logger.info(Thread.currentThread().getName() + "hello ");
                return "hello ";
            }
        });

        jobs.add(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Util.sleep(1);
                logger.info(Thread.currentThread().getName() + "world");
                return "world ";
            }
        });

        try {
            /// 스레드풀에 대량으로 작업을 추가할 때, invokeAll 을 사용함.
            List<Future> futureList =  executorService.invokeAll((Collection)jobs);
            for(Future future : futureList ) {
                logger.info(Thread.currentThread().getName() + " : " + future.get());
            }

            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException  e) {
            e.printStackTrace();
        }

        done = true;
    }

    /// 5개의 스레드풀로 작업을 실행하고, 결과값을 Future 객체를 통해서 확인함.
    /// 본 예제에서는 world가 먼저 출력이 된다.
    public static void submit() {
        done = false;
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        final Collection<Callable<String>> jobs = new ArrayList<Callable<String>>();

        jobs.add(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Util.sleep(3);
                logger.info(Thread.currentThread().getName() + "hello ");
                return "hello ";
            }
        });

        jobs.add(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Util.sleep(1);
                logger.info(Thread.currentThread().getName() + "world");
                return "world ";
            }
        });

        List<Future> futureList = new ArrayList<Future>();

        jobs.forEach(job -> {

            /// 스레드풀에 한개씩 작업을 추가할 때, submit 을 사용함.
            /// 실행 결과를 Future 형태로 리턴합니다.
            futureList.add(executorService.submit(job));
        });

        try {
            for(Future future : futureList ) {
                logger.info(Thread.currentThread().getName() + " : " + future.get());
            }

            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        done = true;
    }

    /*
    ExecutorService newFixedThreadPool(int nThreads)
    최대 지정한 개수 만큼의 쓰레드를 가질 수 있는 쓰레드 풀을 생성한다.
    실제 생성되는 객체는 ThreadPoolExecutor 객체이다.

    ScheduledExecutorService newScheduledThreadPool(int corePoolSize)
    지정한 개수만큼 쓰레드가 유지되는 스케줄 가능한 쓰레드 풀을 생성한다.
    실제 생성되는 객체는 ScheduledThreadPoolExecutor 객체이다.

    ExecutorService newSingleThreadExecutor()
    하나의 쓰레드만 사용하는 ExecutorService를 생성한다.

    ScheduledExecutorService newSingleThreadScheduledExecutor()
    하나의 쓰레드만 사용하는 ScheduledExecutorService를 생성한다.

    ExecutorService newCachedThreadPool()
    필요할 때 마다 쓰레드를 생성하는 쓰레드 풀을 생성한다.
    이미 생성된 쓰레드의 경우 재사용된다.
    실제 생성되는 객체는 ThreadPoolExecutor 객체이다.
    */
}

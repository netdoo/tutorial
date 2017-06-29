package com.exfuturetask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class App {
    final static Logger logger = LoggerFactory.getLogger(App.class);
    public static void main( String[] args ) {

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Integer> longTask = executorService.submit(new FutureTask());

        /// 0.3초 뒤에 longTask 작업을 취소함.
        try {
            Thread.sleep(300);
            logger.info("cancel long task");
            longTask.cancel(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.info("catch InterruptedException");
        } catch (CancellationException e) {
            e.printStackTrace();
            logger.info("catch CancellationException");
        }

        logger.info("shutdown thread pool");

        /// shutdown() 메소드 호출전까지 추가된 task 를 처리함.
        executorService.shutdown();

        /// 추가된 task가 완료될때까지 10분간 기다림.
        try {
            executorService.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.info("exit main thread");
    }
}

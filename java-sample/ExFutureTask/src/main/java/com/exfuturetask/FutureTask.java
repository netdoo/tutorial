package com.exfuturetask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class FutureTask implements Callable<Integer> {
    final static Logger logger = LoggerFactory.getLogger(FutureTask.class);
    public Integer call() throws Exception {

        logger.info("start thread");
        int i = 0, curr = 0;

        while (i < 999999999) {
            if (Thread.currentThread().isInterrupted()) {
                logger.info("외부에서 cancel 된 경우, 쓰레드 Job 을 중지함");
                break;
            } else {
                // 작업 처리
                curr = i++;
                logger.info("{}", curr);
            }
        }

        postWork();
        logger.info("finish thread");
        return new Integer(curr);
    }

    private void postWork() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

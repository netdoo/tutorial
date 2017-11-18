package com.exvertx;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017-11-18.
 */
public class Util {
    final static Logger logger = LoggerFactory.getLogger(Util.class);

    public static void waitAfterSafeStop(long waitSecs, NetServer server, Vertx vertx) {

        server.listen(netServerAsyncResult -> logger.info("start server"));

        try {
            Thread.sleep(waitSecs * 1000);

            server.close(result -> {
                if (result.succeeded()) {
                    logger.info("stop server");
                }
            });

            CountDownLatch countDownLatch = new CountDownLatch(1);

            vertx.close(result -> {
                logger.info("close vert.x");
                countDownLatch.countDown();
            });

            countDownLatch.await(10_000, TimeUnit.SECONDS);
            logger.info("exit main thread");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

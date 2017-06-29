package com.exjava;

import org.apache.log4j.Logger;

public class Main {
    protected static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
//        ExLambda.normal();
//        ExLambda.lambda();
//        ExThread.normal();
//        ExThread.lambda();
//        ExThreadPool.singleRunnable();
//        ExThreadPool.singleCallable();
//        ExThreadPool.invokeAll();
//        ExThreadPool.submit();
//        while (false == ExThreadPool.done) {
//            Util.sleep(1);
//        }

//        ExCompletableFuture.runAsync();

        //ExCompletableFuture.supplyAsync();
        //ExCompletableFuture.composeAsync();
        //ExCompletableFuture.combineAsync();
        //ExCompletableFuture.acceptBoth();
        ExCompletableFuture.applyToEither();
        logger.info("now exit main()");
    }
}

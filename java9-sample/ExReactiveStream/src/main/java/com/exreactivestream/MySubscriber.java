package com.exreactivestream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.Flow;

/**
 * Created by jhkwon78 on 2017-11-29.
 */
public class MySubscriber implements Flow.Subscriber<String> {
    private Flow.Subscription subscription;
    final static Logger logger = LoggerFactory.getLogger(MySubscriber.class);

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        logger.info("onSubscribe {}", subscription);
        this.subscription = subscription;
        subscription.request(1);
    }

    @Override
    public void onNext(String item) {
        logger.info("item {}", item);
        this.subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        logger.info("error: " + throwable);
    }

    @Override
    public void onComplete() {
        logger.info("onComplete");
    }
}
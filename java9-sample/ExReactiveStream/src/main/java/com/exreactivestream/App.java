package com.exreactivestream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class App {
    final static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        SubmissionPublisher<String> sb = new SubmissionPublisher<>(executor, Flow.defaultBufferSize());
        sb.subscribe(new MySubscriber());
        sb.submit("item 1");
        sb.submit("item 2");
        sb.submit("item 3");

        sb.close();

        executor.shutdown();
    }
}


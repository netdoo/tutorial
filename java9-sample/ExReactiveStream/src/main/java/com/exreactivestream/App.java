package com.exreactivestream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class App {
    final static Logger logger = LoggerFactory.getLogger(App.class);

    static void test1() {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        SubmissionPublisher<String> sb = new SubmissionPublisher<>(executor, Flow.defaultBufferSize());
        sb.subscribe(new MySubscriber());
        sb.submit("item 1");
        sb.submit("item 2");
        sb.submit("item 3");

        sb.close();

        executor.shutdown();
    }

    static void test2() throws Exception {
        //Create Publisher
        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
        //Create Processor and Subscriber
        MyFilterProcessor<String, String> firstFilter = new MyFilterProcessor<>(s -> StringUtils.isNumeric(s));
        MyFilterProcessor<String, String> secondFilter = new MyFilterProcessor<>(s -> Integer.parseInt(s) > 1);


        MySubscriber<String> subscriber = new MySubscriber<>();
        //Chain Processor and Subscriber
        publisher.subscribe(firstFilter);
        firstFilter.subscribe(secondFilter);
        secondFilter.subscribe(subscriber);

        System.out.println("Publishing Items...");
        String[] items = {"1", "x", "2", "x", "3", "x"};
        Arrays.asList(items).stream().forEach(i -> publisher.submit(i));
        publisher.close();
        Thread.sleep(2000);
    }

    public static void main( String[] args ) throws Exception {
        test2();
    }
}


package com.exbolts;

import bolts.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class App {

    final static Logger logger = LoggerFactory.getLogger(App.class);

    static void sleepMillSec(int millisecs) {
        try {
            Thread.sleep(millisecs);
        } catch (Exception e) {}
    }

    static String foo() {
        logger.info("Thread {} Start foo", Thread.currentThread().getId());
        sleepMillSec(1_000);
        logger.info("Thread {} Finish foo", Thread.currentThread().getId());
        return "foo";
    }

    static String bar() {
        logger.info("Thread {} Start bar", Thread.currentThread().getId());
        sleepMillSec(1_500);
        logger.info("Thread {} Finish bar", Thread.currentThread().getId());
        return "bar";
    }

    static String zoo() {
        logger.info("Thread {} Start zoo", Thread.currentThread().getId());
        sleepMillSec(700);
        logger.info("Thread {} Finish zoo", Thread.currentThread().getId());
        return "zoo";
    }

    public static void main( String[] args ) {
        Task<String> fooTask = Task.callInBackground(() -> foo());
        Task<String> barTask = Task.callInBackground(() -> bar());
        Task<String> zooTask = Task.callInBackground(() -> zoo());

        List<Task<String>> tasks = Arrays.asList(fooTask, barTask, zooTask);
        try {
            /// 동기함수가 병렬로 실행됨.
            Task.whenAll(tasks).waitForCompletion();

            List<Task> errorTasks = tasks.stream().filter(Task::isFaulted).collect(Collectors.toList());
            if (!errorTasks.isEmpty()) {
                errorTasks.forEach(errorTask ->
                        logger.error("task error {}", errorTask.getError())
                );
            }

            logger.info("foo {} bar {} zoo {}", fooTask.getResult(), barTask.getResult(), zooTask.getResult());

        } catch(InterruptedException e) {
            logger.error("error {}", e.getMessage());
        }
    }
}

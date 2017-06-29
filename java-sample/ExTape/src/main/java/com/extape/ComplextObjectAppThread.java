package com.extape;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.tape2.ObjectQueue;
import com.squareup.tape2.QueueFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ComplextObjectAppThread {
    final static Logger logger = LoggerFactory.getLogger(ComplextObjectAppThread.class);
    final static int MAX_THREAD = 1;
    final static int MAX_REPEAT = 1000;

    class WriteThread extends Thread implements Runnable {
        ObjectMapper objectMapper;
        ObjectQueue<Car> queue;

        public WriteThread(ObjectMapper objectMapper, ObjectQueue<Car> queue) {
            this.objectMapper = objectMapper;
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < MAX_REPEAT; i++) {
                    queue.add(new Car(i, "Insert Data " + i));
                }
            } catch (Exception e) {
                logger.info("e {}", e.getMessage());
            }
        }
    }

    public void multithreadWriters() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        QueueFile queueFile = new QueueFile.Builder(new File("C:\\temp\\ComplexObject.q")).build();
        ObjectQueue<Car> queue = ObjectQueue.create(queueFile, new ComplexObjectConverter(objectMapper));

        WriteThread writeThread = new WriteThread(objectMapper, queue);
        ArrayList<WriteThread> threads = new ArrayList<>();
        LocalDateTime start = LocalDateTime.now();

        logger.info("start thread");
        for (int i = 0; i < MAX_THREAD; i++) {
            writeThread.run();
            threads.add(writeThread);
        }

        for (WriteThread thread : threads) {
            thread.join();
        }

        LocalDateTime finish = LocalDateTime.now();
        logger.info("finish thread, ComplexObject Size {} Elapsed Time {} (secs)", queue.size(), ChronoUnit.SECONDS.between(start, finish));

        queue.clear();
        queue.close();
    }

    public static void main( String[] args ) throws Exception {
        ComplextObjectAppThread complextObjectAppThread = new ComplextObjectAppThread();
        complextObjectAppThread.multithreadWriters();
    }
}

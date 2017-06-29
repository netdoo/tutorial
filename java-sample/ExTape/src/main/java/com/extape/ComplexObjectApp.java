package com.extape;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.tape2.ObjectQueue;
import com.squareup.tape2.QueueFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

public class ComplexObjectApp {
    final static Logger logger = LoggerFactory.getLogger(ComplexObjectApp.class);

    public static void main( String[] args ) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        QueueFile queueFile = new QueueFile.Builder(new File("C:\\temp\\ComplexObject.q")).build();
        ObjectQueue<Car> queue = ObjectQueue.create(queueFile, new ComplexObjectConverter(objectMapper));

        /// 큐에, 데이터를 추가함.
        queue.add(new Car(111, "K3"));
        queue.add(new Car(222, "K5"));
        queue.add(new Car(333, "K6"));
        queue.add(new Car(444, "K7"));
        queue.add(new Car(555, "K9"));

        /// 큐의 첫번째 데이터를 조회함. [K3]
        Car car = queue.peek();
        logger.info("car {}", car);

        /// 큐의 첫번째부터 두번째까지의 데이터를 조회함. [K3, K5]
        List<Car> carList = queue.peek(2);
        logger.info("carList {}", carList);

        /// 큐의 모든 데이터를 조회함. [K3, K5, K6, K7, K9]
        List<Car> allCarList = queue.asList();
        logger.info("allCarList {}", allCarList);

        /// 큐의 첫번째 데이터를 삭제함. [K3]
        queue.remove();

        /// 큐의 첫번째부터 두번째까지의 데이터를 삭제함. [K5, K6]
        queue.remove(2);

        /// 큐의 모든 데이터를 삭제함.
        // queue.clear();

        /// 큐 안에 있는 데이터를 순회함. [K7, K9]
        Iterator<Car> iterator = queue.iterator();
        while (iterator.hasNext()) {
            Car c = iterator.next();
            logger.info("element {}", c);
        }

        queue.close();
    }
}

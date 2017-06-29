package com.extape;

import com.squareup.tape2.QueueFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Iterator;

public class BasicApp {
    final static Logger logger = LoggerFactory.getLogger(BasicApp.class);

    public static void main( String[] args ) throws Exception {
        File file = new File("C:\\temp\\basic.q");
        QueueFile queueFile = new QueueFile.Builder(file).build();

        /// queueFile에 one, two, three 데이터를 추가함.
        queueFile.add("one".getBytes());
        queueFile.add("two".getBytes());
        queueFile.add("thee".getBytes());

        /// queue size 3이 출력됨
        logger.info("queue size {}", queueFile.size());

        /// data one queue size 3이 출력됨.
        byte[] data = queueFile.peek();
        logger.info("data {} queue size {}", new String(data), queueFile.size());

        /// 가장 첫번째 큐 데이터를 제거함.
        queueFile.remove();

        /// 큐 안에 있는 데이터를 순회함. [two, three]
        Iterator<byte[]> iterator = queueFile.iterator();
        while (iterator.hasNext()) {
            byte[] element = iterator.next();
            logger.info("{}", new String(element));
        }

        /// queue size 2 가 출력됨.
        logger.info("queue size {}", queueFile.size());

        /// queue 안의 모든 데이터를 제거함.
        queueFile.clear();
        queueFile.close();
    }
}

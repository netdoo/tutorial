package com.extape;

import com.squareup.tape2.ObjectQueue;
import com.squareup.tape2.QueueFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

public class ObjectApp {
    final static Logger logger = LoggerFactory.getLogger(ObjectApp.class);

    public static void main( String[] args ) throws Exception {
        QueueFile queueFile = new QueueFile.Builder(new File("C:\\temp\\object.q")).build();
        ObjectQueue<String> queue = ObjectQueue.create(queueFile, new ObjectQueue.Converter<String>() {
            @Override
            public String from(byte[] bytes) throws IOException {
                return new String(bytes);
            }

            @Override
            public void toStream(String s, OutputStream outputStream) throws IOException {
                outputStream.write(s.getBytes());
            }
        });

        /// 큐에 [111, 222, 333, 444, 555] 데이터를 추가함.
        queue.add("111");
        queue.add("222");
        queue.add("333");
        queue.add("444");
        queue.add("555");

        /// 큐의 첫번째 데이터를 조회함.
        String data = queue.peek();
        logger.info("data {}", data);

        /// queue 첫번째 부터 두번째까지의 데이터를 조회함. [111, 222]
        List<String> dataList = queue.peek(2);
        logger.info("dataList {}", dataList);

        /// 큐의 모든 데이터를 조회함. [111, 222, 333, 444, 555]
        List<String> allDataList = queue.asList();
        logger.info("allDataList {}", allDataList);

        /// 큐의 첫번째 데이터를 삭제함. 111 이 삭제됨.
        queue.remove();

        /// 큐의 첫번째 부터 두번째 까지의 데이터를 삭제함. [222, 333]이 삭제됨.
        queue.remove(2);

        /// 큐의 모든 데이터를 삭제함.
        /// queue.clear();

        /// 큐 안에 있는 데이터를 순회함. [444, 555]
        Iterator<String> iterator = queue.iterator();
        while (iterator.hasNext()) {
            String element = iterator.next();
            logger.info("element {}", element);
        }

        queue.close();
    }
}


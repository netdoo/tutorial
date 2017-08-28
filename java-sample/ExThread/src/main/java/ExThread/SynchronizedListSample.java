package ExThread;


import com.sun.deploy.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SynchronizedListSample {
    static Logger logger = LoggerFactory.getLogger(SynchronizedListSample.class);
    public static void main(String[] args) throws Exception {

        //List<String> dataList = Collections.synchronizedList(new ArrayList<String>());
        List<String> dataList = new CopyOnWriteArrayList<>(new ArrayList<String>());

        dataList.add("패션");
        dataList.add("뷰티");
        dataList.add("식품");
        dataList.add("유아동");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                    logger.info("ADD 생활");
                    dataList.add("생활");
                    logger.info("ADD 홈");
                    dataList.add("홈");
                    Thread.sleep(200);
                    logger.info("ADD 가전");
                    dataList.add("가전");
                    logger.info("ADD 디지털");
                    dataList.add("디지털");
                } catch (Exception e) {}
            }
        }).start();

        /// 데이터를 조회하는 동안, 또 다른 스레드에서 데이터가 추가되더라도,
        /// ConcurrentModificationException 이 발생하지는 않지만, 추가된 데이터를
        /// 확인하기 위해서는, 다시한번 iterate를 수행해야 함.
        for (String data : dataList) {
            logger.info("READ {}", data);
            Thread.sleep(100);
        }
    }
}

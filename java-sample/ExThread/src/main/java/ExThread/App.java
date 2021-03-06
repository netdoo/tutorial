package ExThread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class App {
    static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ) throws Exception {

        logger.info("Active Thread Count : " + Thread.activeCount());

        // 쓰레드를 최대 3개까지 만드는 쓰레드풀 생성
        ExecutorService p = Executors.newFixedThreadPool(5, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                logger.info("create thread " + t.getId());
                return t;
            }
        });

        try {
            for(int i = 0 ; i < 10; i++){
                /// 10개의 task를 추가함.
                p.execute(new Runnable() {
                    @Override
                    public void run() {
                        Random random = new Random();

                        try {
                            logger.info(Thread.currentThread().getName() + "  start");
                            Thread.sleep(random.nextInt(5) * 1000);
                            logger.info(Thread.currentThread().getName() + "  end");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /// shutdown() 메소드 호출전까지 추가된 task 를 처리함.
        p.shutdown();

        logger.info("Before Active Thread Count : " + Thread.activeCount());

        /// 추가된 task가 완료될때까지 10분간 기다림.
        p.awaitTermination(10, TimeUnit.MINUTES);

        Thread.sleep(1000);
        logger.info("After Active Thread Count : " + Thread.activeCount());

        RuntimeMXBean rt = ManagementFactory.getRuntimeMXBean();
        String processID = rt.getName();
        processID = processID.substring(0, processID.indexOf("@"));
        logger.info("Exit Main " + processID);
    }
}

package ExThread;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase
{
    protected static Logger logger = LoggerFactory.getLogger(AppTest.class);
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }


    class MyRunnable implements Runnable {
        String id;

        public MyRunnable(String id) {
            this.id = id;
        }

        @Override
        public void run() {
            logger.info("{} is start", this.id);
            logger.info("{} is done", this.id);
        }
    }


    /**
     * 스레드풀 테스트 :-)
     */
    public void testApp() throws Exception  {
        logger.info("Active Thread Count : " + Thread.activeCount());

        // 쓰레드를 최대 5개까지 만드는 쓰레드풀 생성
        ExecutorService p = Executors.newFixedThreadPool(5);

        for(int i = 0 ; i < 10; i++) {
            /// 10개의 task를 추가함.
            p.execute(new Runnable() {
                @Override
                public void run() {
                    logger.info(Thread.currentThread().getName() + "  start");
                    logger.info(Thread.currentThread().getName() + "  end");
                }
            });
        }

        p.execute(new MyRunnable("another job"));

        /// shutdown() 메소드 호출전까지 추가된 task 를 처리함.
        p.shutdown();

        logger.info("Before Active Thread Count : " + Thread.activeCount());

        /// 추가된 task가 완료될때까지 10분간 기다림.
        p.awaitTermination(10, TimeUnit.MINUTES);

        Thread.sleep(1000);
        logger.info("After Active Thread Count : " + Thread.activeCount());
   }
}

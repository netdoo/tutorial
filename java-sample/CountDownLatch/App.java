
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class App { 

    public static void doSomething() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {

        final int maxThreadCount = 3;
        final CountDownLatch latch = new CountDownLatch(maxThreadCount);

        System.out.println("start");
        for (int i = 0; i  <  maxThreadCount; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getId() + " start");
                doSomething();
                System.out.println(Thread.currentThread().getId() + " done");
                latch.countDown();
                System.out.println("remain thread " + latch.getCount());
            }).start();
        }

        /// latch.getCount() == 0 이 될때까지 10초 동안 기다림.
        latch.await(10, TimeUnit.SECONDS);
        System.out.println("finish");
    }
}

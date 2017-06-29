package ExThread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

public class AtomicBooleanExample {
    static Logger logger = LoggerFactory.getLogger(AtomicBooleanExample.class);

    public static void main(String[] args) throws Exception {
        final AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        for (int i = 0; i < 10; i++) {

            Thread t1 = new Thread("T1") {
                public void run() {

                    int i = 0;
                    while(i++ < 30) {
                        logger.info("{} Waiting for T2 to set Atomic variable to true. Current value is {}", Thread.currentThread().getName(), atomicBoolean.get());
                        if (atomicBoolean.compareAndSet(true, false)) {
                            logger.debug("******* {} Current value {}", Thread.currentThread().getName(), atomicBoolean.get());
                            break;
                        }
                    }
                };
            };

            t1.start();

        }




        Thread t2 = new Thread("T2") {
            public void run() {
                logger.info(Thread.currentThread().getName() + " is setting the variable to true");
                atomicBoolean.set(true);
            };
        };

        t2.start();

        t2.join();
        Thread.sleep(10_000);
    }
}

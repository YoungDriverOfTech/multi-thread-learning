package org.example.threadmethod;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class JoinDemo {
    static int number = 0;

    @Test
    public void joinDemo() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(10);
                log.info("start");
                number = 10;
                log.info("end");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "t1");
        t1.start();

        // 主线程会等待t1执行完之后，在执行
        t1.join();
        log.info("{}", number);
    }
}

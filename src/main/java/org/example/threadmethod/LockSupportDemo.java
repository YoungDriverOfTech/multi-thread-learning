package org.example.threadmethod;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.locks.LockSupport;

@Slf4j
public class LockSupportDemo {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.info("running");

            // 当打断状态是false的时候才会生效. 当执行到这里，线程阻塞。 当主线程进行打断后，会继续执行
            LockSupport.park();

            // Thread.interrupted() 这个方法会在返回打断标志以后，清除标志
            log.info("status: {}", Thread.interrupted());
            log.info("stop");

            // 因为上面的Thread.interrupted()会清除打断标志，下面这行会再次阻塞
            LockSupport.park();
            log.info("stop2----");
        }, "t1");

        t1.start();

        Thread.sleep(1);
        log.info("start interrupt");
        t1.interrupt();
    }
}

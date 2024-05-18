
package org.example.threadmethod;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

@Slf4j
public class SleepAndYield {

    @Test
    public void sleepStateDemo() throws InterruptedException {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        t1.start();

        // t1 state RUNNABLE, 因为启动t1线程后，主线程先执行，但是t1还没开始执行，所以状态不是running
        log.info("t1 state {}", t1.getState());
        Thread.sleep(500);
        // t1 state TIMED_WAITING，因为主线程在sleep，cpu去执行其他线程，正巧t1线程也正在sleep，所以状态是TIMED_WAITING
        log.info("t1 state {}", t1.getState());
    }


    @Test
    public void sleepInterruptDemo() throws InterruptedException {
        // t1 线程启动后，进去sleep。然后主线程使用interrupt方法，打断t1的垂面
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                try {
                    log.info("enter sleep");
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    log.info("wake up");
                    throw new RuntimeException(e);
                }
            }
        };
        t1.start();

        // 主线程打断t1睡眠
        Thread.sleep(500);
        log.info("start interrupt");
        t1.interrupt();
    }

    @Test
    public void timeUnitSleepDemo() throws InterruptedException {
        log.info("start");
        TimeUnit.SECONDS.sleep(2); // 这种写法，可读性更好，比起Thread.sleep(500);
        log.info("end");
    }
}

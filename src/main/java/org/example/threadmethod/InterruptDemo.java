package org.example.threadmethod;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class InterruptDemo {

    @Test
    public void interruptDemo() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.info("start sleep");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();

                // 因为当前线程本别的线程打断，所以打断flag是false
                // 当interrupt()方法打断一个正在睡眠的线程时，该线程会抛出InterruptedException，并且会清除中断状态（即isInterrupted()会返回false）
                log.info("Interrupted status of t1: {}", Thread.currentThread().isInterrupted());
            }
        }, "t1");
        t1.start();

        Thread.sleep(1000);

        log.info("start interrupt");

        // 主线程调用了 interrupt方法，会设置t1线程的打断状态，所以下面的语句会打印出来true
        t1.interrupt();
        log.info("end interrupt, flag: {}", t1.isInterrupted());
    }
}

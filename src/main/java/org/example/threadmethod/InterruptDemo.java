package org.example.threadmethod;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class InterruptDemo {

    @Test
    public void interruptBlockDemo() throws InterruptedException {
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

        // 主线程调用了 interrupt方法，会设置t1线程的打断状态，但是因为打断的是阻塞状态，所以在t1线程内的打断标志是false
        t1.interrupt();
    }

    @Test
    public void interruptNormalRunningDemo() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (true) {
                boolean interrupted = Thread.currentThread().isInterrupted();
                if (interrupted) {
                    log.info("interrupt by other threads, execution finished");
                    break;
                }
            }
        }, "t1");
        t1.start();

        Thread.sleep(500);
        log.info("start interrupt");
        // 主线程调用了 interrupt方法，会设置t1线程的打断状态. 但是这里的interrupt方法只是设置了打断的标记，并不会终止
        // 被打断线程的执行，需要在t1线程内根据打断标记进行相应的逻辑处理
        t1.interrupt();
    }


}

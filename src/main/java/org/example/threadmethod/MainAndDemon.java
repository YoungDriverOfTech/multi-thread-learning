package org.example.threadmethod;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainAndDemon {

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            while (true) {
                boolean interrupted = Thread.currentThread().isInterrupted();
                if ((interrupted)) {
                    break;
                }
            }
        }, "t1");

        // 如果设置了这个线程为守护线程， 那么当主线程执行完毕之后，t1线程会强制结束
        // t1.setDaemon(true);
        t1.start();

        log.info("end");
    }
}

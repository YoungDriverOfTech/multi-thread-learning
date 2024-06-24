package org.example.semaphore;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;

@Slf4j
public class SemaphoreDemo {
    public static void main(String[] args) {
        // 1. 创建semaphore对象
        Semaphore semaphore = new Semaphore(3);

        // 2. 10个线程同时运行
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    // 获得许可
                    semaphore.acquire();
                    log.info("running...");
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    // 释放许可
                    semaphore.release();
                    log.info("end...");
                }
            }, "线程-" + i).start();
        }
    }
}

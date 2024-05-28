package org.example.reentrantlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class TryLockDemo {
    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {

            log.info("trying to get the lock");

            // lock.tryLock() 获取锁成功的话，返回true，都则返回false
            // lock.tryLock(long n) 可以指定等待时间
            if (!lock.tryLock()) {
                log.info("haven't got the lock");
                return;
            }

            try {
                log.info("got the lock");
            } finally {
                lock.unlock();
            }
        }, "t1");

        // 主线程 先加锁
        lock.lock();

        // t1线程在启动
        t1.start();
    }
}

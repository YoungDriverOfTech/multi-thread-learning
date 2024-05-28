package org.example.reentrantlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ReentrantLockDemo {
    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {

            try {
                // 如果没有竞争，那么此方法就会获取lock对象锁
                // 如果有竞争就进入阻塞队列，可以被其他线程用interrupt方法打断
                log.info("trying to get the lock");
                lock.lockInterruptibly();
            } catch (InterruptedException e) {
                e.printStackTrace();

                // 一旦被打断就会进入catch块
                log.info("can not get lock");
                return; // 因为没有获得锁，所以无法进入同步代码块执行逻辑，只能现行return
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

        // 主线程打断t1线程
        Thread.sleep(1000);
        t1.interrupt();

    }
}

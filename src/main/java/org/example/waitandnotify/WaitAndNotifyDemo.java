package org.example.waitandnotify;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WaitAndNotifyDemo {

    private static Object obj = new Object();

    public static void main(String[] args) throws InterruptedException {

        new Thread(() -> {
            // t1 获取到锁
            synchronized (obj) {
                try {

                    // t1 进入waitset，等待被唤醒。然后释放锁obj，此时obj可以被其他线程获取
                    obj.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            log.info("t1 do things");
        }, "t1").start();

        new Thread(() -> {
            synchronized (obj) {
                try {
                    // t2 进入waitset，等待被唤醒。然后释放锁obj，此时obj可以被其他线程获取
                    obj.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            log.info("t2 do things");
        }, "t2").start();

        Thread.sleep(2000);

        // 主线程获取到锁，然后唤醒waitset中的其他线程
        synchronized (obj) {
            // obj.notify(); // 唤醒一个线程
            obj.notifyAll(); // 唤醒所有线程，让他们再到entryList排队去，重新获取锁
        }
    }
}

package org.example.readwritelock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读-读 可并发
 * 写-写/读-写 不可并发（互斥）
 */
@Slf4j
public class ReadWriteLockDemo {
    public static void main(String[] args) {
        DataContainer dataContainer = new DataContainer();

        new Thread(() -> {
            dataContainer.read();
        }, "t1").start();

        new Thread(() -> {
            dataContainer.write();
        }, "t2").start();
    }

}

@Slf4j
class DataContainer {
    private Object data;
    private ReentrantReadWriteLock rw = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock r = rw.readLock();
    private ReentrantReadWriteLock.WriteLock w = rw.writeLock();

    public Object read() {
        log.info("获取读锁...");

        r.lock();
        try {
            log.info("读取");
            Thread.sleep(1);
            return data;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            log.info("释放读锁");
            r.unlock();
        }
    }

    public void write() {
        log.info("获取写锁");
        w.lock();
        try {
            log.info("写入");
        } finally {
            log.info("释放写锁");
            w.unlock();
        }
    }
}

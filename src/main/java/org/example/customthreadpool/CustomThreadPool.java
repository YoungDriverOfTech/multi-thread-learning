package org.example.customthreadpool;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CustomThreadPool {
}

class BlockingQueue<T> {
    // 1. 任务队列
    private Deque<T> queue = new ArrayDeque<>();

    // 2. 锁
    private ReentrantLock lock = new ReentrantLock();

    // 3. 生产者条件
    private Condition fullWaitSet = lock.newCondition();

    // 4. 消费者条件
    private Condition emptyWaitSet = lock.newCondition();

    // 5. 容量
    private int capacity;

    // 阻塞获取
    public T take() {
        lock.lock();
        try {

            // 如果队列中没任务，那么需要等待生产者生产任务
            while (queue.isEmpty()) {
                try {
                    emptyWaitSet.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            // 消费者消费了一个任务，给队列腾出来空间了，通知生产者那边生产任务
            T t = queue.removeFirst();
            fullWaitSet.signal();
            return t;
        } finally {
            lock.unlock();
        }
    }

    // 阻塞添加
    public void pull(T element) {
        lock.lock();
        try {
            // 队列满了，不让生产者放任务
            while (queue.size() == capacity) {
                try {
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            // 队列不满，可以放，并且放了任务之后，通知消费者，可以消费了
            queue.addLast(element);
            emptyWaitSet.signal();
        } finally {
            lock.unlock();
        }
    }

    // 获取容量
    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }
}

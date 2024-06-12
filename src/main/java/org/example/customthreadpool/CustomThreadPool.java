package org.example.customthreadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class CustomThreadPool {
    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(2, 1000, TimeUnit.MILLISECONDS, 10);
        for (int i = 0; i < 15; i++) {
            int j = i;

            threadPool.execute(() -> {
                try {
                    Thread.sleep(1000000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("当前任务只在被执行： {}", j);
            });
        }
    }
}

@Slf4j
class ThreadPool {
    // 任务队列
    private BlockingQueue<Runnable> taskQueue;

    // 线程集合
    private final HashSet<Worker> workers = new HashSet<>();

    // 核心线程数
    private int coreSize;

    // 获取任务的超时时间
    private long timeout;
    private TimeUnit timeUnit;

    public ThreadPool(int coreSize, long timeout, TimeUnit timeUnit, int queueCapacity) {
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.taskQueue = new BlockingQueue<>(queueCapacity);
    }

    // 执行任务
    public void execute(Runnable task) {
        // 当任务数量还没超过coreSize的时，直接创建一个线程（work对象），去执行任务，并且把线程加入到workers中
        // 如果任务数量超过coreSize，需要把任务装进任务队列暂存
        synchronized (this) {
            if (workers.size() < coreSize) {
                Worker worker = new Worker(task);
                log.info("新增 worker:{}, task:{}", worker, task);
                workers.add(worker);
                worker.start();
            } else {
                log.info("加入任务队列 {}", task);
                taskQueue.put(task);
            }
        }
    }

    class Worker extends Thread{
        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            // 执行任务
            // 1）当task不为空，执行任务
            // 2）当task执行完毕，再接着从任务队列中获取任务并执行
            // 3) 如果当前task执行完毕，任务队列中的task也被执行完了，那么当前的这个work线程就可以被销毁了

            // 注意taskQueue.take()会一直等待，等待生产者唤醒他们继续消费任务，如果想要使用带着过期时间的方法，可以使用poll。
            // 这就是线程池的策略
            // while (task != null || (task = taskQueue.take()) != null) {
            while (task != null || (task = taskQueue.poll(timeout, timeUnit)) != null) {
                try {
                    log.info("正在执行 {}", task);
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    task = null;
                }
            }

            synchronized (workers) {
                log.info("worker被移除 {}", this);
                workers.remove(this);
            }
        }
    }
}

@Slf4j
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

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    // 带超时的阻塞获取
    public T poll(long timeout, TimeUnit unit) {
        lock.lock();
        try {
            // transfer to same time format
            long nanos = unit.toNanos(timeout); // 转换为纳秒

            // 如果队列中没任务，那么需要等待生产者生产任务
            while (queue.isEmpty()) {
                try {
                    // 如果剩余等待时间已经耗尽，那么直接退出
                    if (nanos <= 0) {
                        return null;
                    }

                    // 此方法会返回等待的剩余时间，比如原本要等待5秒，但是在第3秒的时候被虚假唤醒，接下来还需要等待2秒，那么这个2秒就会被返回
                    nanos = emptyWaitSet.awaitNanos(nanos);
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
    public void put(T task) {
        lock.lock();
        try {
            // 队列满了，不让生产者放任务
            while (queue.size() == capacity) {
                try {
                    log.info("等待加入任务队列: {}", task);
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            // 队列不满，可以放，并且放了任务之后，通知消费者，可以消费了
            log.info("加入任务队列: {}", task);
            queue.addLast(task);
            emptyWaitSet.signal();
        } finally {
            lock.unlock();
        }
    }

    // 带超时时间的阻塞添加
    public boolean offer(T task, long timeout, TimeUnit timeUnit) {
        lock.lock();
        try {
            long nanos = timeUnit.toNanos(timeout);

            // 队列满了，不让生产者放任务
            while (queue.size() == capacity) {
                try {
                    log.info("等待加入任务队列: {}", task);
                    if (nanos <= 0) {
                        return false;
                    }

                    nanos = fullWaitSet.awaitNanos(nanos); // 返回剩余时间
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            // 队列不满，可以放，并且放了任务之后，通知消费者，可以消费了
            log.info("加入任务队列: {}", task);
            queue.addLast(task);
            emptyWaitSet.signal();

            return true;
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

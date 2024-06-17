package org.example.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class FixedThreadPoolDemo {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2, new ThreadFactory() {

            private final AtomicInteger atomicInteger = new AtomicInteger(0);

            // 用来创建线程，并且给线程重命名
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "my-pool-" + atomicInteger.getAndIncrement());
            }
        });

        executorService.execute(() -> {
            log.info("test 1");
        });

        executorService.execute(() -> {
            log.info("test 2");
        });

        // 因为只有核心线程，所以第三个任务执行完了以后，程序也不会退出
        executorService.execute(() -> {
            log.info("test 3");
        });
    }
}

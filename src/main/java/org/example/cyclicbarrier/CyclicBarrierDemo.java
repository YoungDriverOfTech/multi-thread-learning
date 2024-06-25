package org.example.cyclicbarrier;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class CyclicBarrierDemo {
    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(2);
        CyclicBarrier barrier = new CyclicBarrier(2, () -> {
           log.info("after all tasks");
        });

        service.submit(() -> {
           log.info("task1 start");
            try {
                barrier.await(); // 让计数减1, 如果计数不减到0，那么就会在这里阻塞
                log.info("task1 end");
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        });

        service.submit(() -> {
            log.info("task2 start");
            try {
                barrier.await(); // 让计数减1，只有计数减到0，再回执行await后面的
                log.info("task2 end");
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        });

        // barrier可以被重复使用，如果再有个线程执行了await方法，那么计数会再次从2开始减
    }
}

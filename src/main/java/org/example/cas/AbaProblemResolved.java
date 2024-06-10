package org.example.cas;

import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

@Slf4j
public class AbaProblemResolved {

    static AtomicStampedReference<String> ref = new AtomicStampedReference<>("A", 0);

    public static void main(String[] args) throws InterruptedException {

        // 主线程获得期望值
        String prev = ref.getReference();
        int stamp = ref.getStamp();
        log.info("prev: {} version: {}", prev, stamp);

        // 其他的线程会修改ref的值，但是最终会修改成和主线程获得的期望值一样
        modifiedRef();
        Thread.sleep(200);

        // 主线程修改的话，会失败，因为版本号对比已经失败了
        boolean result = ref.compareAndSet(prev, "C", stamp, stamp + 1);
        System.out.println("result = " + result);
    }

    private static void modifiedRef() throws InterruptedException {
        new Thread(() -> {
            int stamp = ref.getStamp();
            log.info("change: A -> B");
            log.info("prev: {} version: {}", ref.getReference(), stamp);
            ref.compareAndSet(ref.getReference(), "B", stamp, stamp + 1);
        }, "t1").start();

        Thread.sleep(200);
        new Thread(() -> {
            int stamp = ref.getStamp();
            log.info("change: B -> A");
            log.info("prev: {} version: {}", ref.getReference(), stamp);
            ref.compareAndSet(ref.getReference(), "A", stamp, stamp + 1);
        }, "t2").start();

    }
}

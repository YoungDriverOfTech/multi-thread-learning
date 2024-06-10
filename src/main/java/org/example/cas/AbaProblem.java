package org.example.cas;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class AbaProblem {

    static AtomicReference<String> ref = new AtomicReference<>("A");

    public static void main(String[] args) throws InterruptedException {

        // 主线程获得期望值
        String prev = ref.get();

        // 其他的线程会修改ref的值，但是最终会修改成和主线程获得的期望值一样
        modifiedRef();
        Thread.sleep(200);

        // 主线程修改的话，会成功，因为期望是确实是一样的，但其实在主线程修改前，发生了两次修改，这个被主线程忽视了
        boolean result = ref.compareAndSet(prev, "C");
        System.out.println("result = " + result);
    }

    private static void modifiedRef() throws InterruptedException {
        new Thread(() -> {
            log.info("change: A -> B");
            ref.compareAndSet(ref.get(), "B");
        }, "t1").start();

        Thread.sleep(200);
        new Thread(() -> {
            log.info("change: B -> A");
            ref.compareAndSet(ref.get(), "A");
        }, "t2").start();

    }
}

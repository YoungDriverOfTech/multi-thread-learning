package org.example.designpattern;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GuardedSuspensionDemo {
    public static void main(String[] args) {
        GuardedObject guarded = new GuardedObject();

        // 线程1 已进入逻辑，直接wait，等待结果
        new Thread(() -> {
            try {
                Object result = guarded.get();
                log.info("result: {}", result);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }, "t1").start();

        // 线程2 进入逻辑生产一个字符串给线程1
        new Thread(() -> {
            guarded.set("wahahahahahhaha");
        }, "t2").start();


    }
}

/**
 * 线程1需要从线程2中获取结果
 */
@Slf4j
class GuardedObject {
    private Object guardedObject;

    // 线程1获取线程2的结果
    public Object get() throws InterruptedException {
        synchronized (this) {
            while (guardedObject == null) {
                log.info("线程2，还没把结果传过来");
                this.wait();
            }
        }

        return guardedObject;
    }

    // 线程2生产结果，放到guardedObject中
    public void set(Object object) {
        synchronized (this) {
            log.info("生产好了");
            this.guardedObject = object;
            this.notify();
        }
    }
}
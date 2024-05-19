package org.example.threadmethod;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SixThreadState {

    public static void main(String[] args) {
        // New
        Thread t1 = new Thread(() -> {

        }, "t1");

        // Runnable
        Thread t2 = new Thread(() -> {
            synchronized (SixThreadState.class) {
                while (true) {

                }
            }
        }, "t2");
        t2.start();

        // Terminated
        Thread t3 = new Thread(() -> {

        }, "t3");
        t3.start();

        // Timed Waiting
        Thread t4 = new Thread(() -> {
            try {
                Thread.sleep(1000000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "t4");
        t4.start();

        // Waiting
        Thread t5 = new Thread(() -> {
            try {
                t2.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "t5");
        t5.start();

        // Blocked
        Thread t6 = new Thread(() -> {
            try {
                synchronized (SixThreadState.class) {
                    Thread.sleep(1000000);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "t6");
        t6.start();

        log.info("t1 State: {}", t1.getState());
        log.info("t2 State: {}", t2.getState());
        log.info("t3 State: {}", t3.getState());
        log.info("t4 State: {}", t4.getState());
        log.info("t5 State: {}", t5.getState());
        log.info("t6 State: {}", t6.getState());

//        2024-05-19 17:11:10  [ main:0 ] - [ INFO ]  t1 State: NEW
//        2024-05-19 17:11:10  [ main:1 ] - [ INFO ]  t2 State: RUNNABLE
//        2024-05-19 17:11:10  [ main:1 ] - [ INFO ]  t3 State: TERMINATED
//        2024-05-19 17:11:10  [ main:1 ] - [ INFO ]  t4 State: TIMED_WAITING
//        2024-05-19 17:11:10  [ main:1 ] - [ INFO ]  t5 State: WAITING
//        2024-05-19 17:11:10  [ main:1 ] - [ INFO ]  t6 State: BLOCKED
    }
}

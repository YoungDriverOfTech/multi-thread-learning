package org.example.createthread;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreateThread2 {
    public static void main(String[] args) {
        // Create task
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                log.info("task run");
            }
        };

        // Create thread & put task into thread & put thread name
        Thread t1 = new Thread(runnable, "t1");
        t1.start();

        // Main thread
        log.info("main run");
    }
}

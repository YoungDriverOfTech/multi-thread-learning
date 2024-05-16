package org.example.createthread;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CreateThread1 {
    public static void main(String[] args) {
        // Create thread
        Thread t1 = new Thread(){
            @Override
            public void run() {
                log.info("t1 running");
            }
        };
        t1.setName("t1"); // Set thread name
        t1.start(); // 启动线程

        // Main thread
        log.info("main running");
    }
}

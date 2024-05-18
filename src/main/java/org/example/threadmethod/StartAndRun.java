package org.example.threadmethod;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StartAndRun {
    public static void main(String[] args) {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                log.info("running");
            }
        };

        t1.start(); // 会开启新线程并且调用run方法
        t1.run(); // 不会开启新线程，主方法来运行run方法
    }
}

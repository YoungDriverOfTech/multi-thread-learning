package org.example.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class InvokeAllDemo {

    public static void main(String[] args) throws Exception{
        ExecutorService pool = Executors.newFixedThreadPool(2);

        List<Future<Object>> futures = pool.invokeAll(Arrays.asList(
                () -> {
                    log.info("begin");
                    Thread.sleep(1000);
                    return "1";
                },

                () -> {
                    log.info("begin");
                    Thread.sleep(1000);
                    return "2";
                },

                () -> {
                    log.info("begin");
                    Thread.sleep(1000);
                    return "3";
                }
        ));

        for (Future<Object> future : futures) {
            System.out.println("future.get() = " + future.get());
        }
    }
}

package org.example.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class SubmitTaskDemo {

    public static void main(String[] args) throws Exception{
        ExecutorService pool = Executors.newFixedThreadPool(2);

        // submit 方法，接受task的返回值
        Future<String> future = pool.submit(() -> {
            log.info("running");
            Thread.sleep(1000);
            return "ok";
        });
        log.info("{}", future.get());

    }
}

package org.example.createthread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Slf4j
public class CreateThread3 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 使用Future task + Callable接口完成又返回值的task创建
        FutureTask<Integer> task = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.info("future task running");
                Thread.sleep(1000);
                return 202;
            }
        });

        // 创建线程，组合任务，执行线程逻辑
        Thread t1 = new Thread(task, "t1");
        t1.start();

        // 为了获取t1线程的结果，主线程会阻塞，知道t1线程执行完毕
        log.info(String.valueOf(task.get()));
    }
}

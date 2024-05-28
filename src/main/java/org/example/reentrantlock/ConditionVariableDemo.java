package org.example.reentrantlock;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class ConditionVariableDemo {
    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {

        // 创建一个新的条件变量
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();

        lock.lock();
        // 进入waitSet，让线程进入休息室(wait set)等待
        condition1.wait();


        // 让线程从休息室(wait set)里面出来,重新竞争锁
        condition1.signal();
        condition1.signalAll();
    }
}

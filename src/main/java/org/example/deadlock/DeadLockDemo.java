package org.example.deadlock;

public class DeadLockDemo {
    public static void main(String[] args) {
        Object a = new Object();
        Object b = new Object();

        new Thread(() -> {
            synchronized (a) {
                try {
                    Thread.sleep(100);

                    synchronized (b) {
                        System.out.println("b = " + b);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "t1").start();

        new Thread(() -> {
            synchronized (b) {
                try {
                    Thread.sleep(50);

                    synchronized (a) {
                        System.out.println("b = " + b);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "t2").start();
    }
}

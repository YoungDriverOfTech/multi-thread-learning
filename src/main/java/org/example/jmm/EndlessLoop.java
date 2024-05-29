package org.example.jmm;

public class EndlessLoop {

    static boolean run = true;
    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            while (run) {
                //
            }
        }).start();

        Thread.sleep(1);

        run = false;
    }
}

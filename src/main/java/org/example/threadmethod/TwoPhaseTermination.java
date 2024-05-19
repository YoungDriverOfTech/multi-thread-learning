package org.example.threadmethod;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TwoPhaseTermination {

    public static void main(String[] args) throws InterruptedException {
        MonitorApp monitorApp = new MonitorApp();

        monitorApp.start();
        Thread.sleep(2000);
        monitorApp.stop();
    }
}

@Slf4j
class MonitorApp {
    private Thread monitor;

    public void start() {
        monitor = new Thread(() -> {
            Thread currentThread = Thread.currentThread();

            while (true) {
                // check interrupt flag and decide if stop workload process
                if (currentThread.isInterrupted()) {
                    log.info("Interrupt, need to stop this workflow");
                    break;
                }

                try {
                    Thread.sleep(200000);
                    // running
                    log.info("running");
                } catch (InterruptedException e) {
                    e.printStackTrace();

                    // Interrupted by other threads when this thread is in block status, then need to reset interrupt flag
                    currentThread.interrupt();
                }
            }
        });

        monitor.start();
    }

    public void stop() {
        monitor.interrupt();
    }
}

package org.example;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) throws InterruptedException {
        while(true) {
            Thread.sleep(2000);
            System.out.println(" = ");
        }
    }
}
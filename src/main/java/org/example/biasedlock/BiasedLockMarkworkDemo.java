package org.example.biasedlock;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

@Slf4j
public class BiasedLockMarkworkDemo {
    public static void main(String[] args) {
        log.info(ClassLayout.parseInstance(new Dog()).toPrintable());
    }


}

class Dog {

}
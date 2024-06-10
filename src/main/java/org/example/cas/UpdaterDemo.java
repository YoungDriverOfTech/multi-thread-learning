package org.example.cas;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

@Slf4j
public class UpdaterDemo {
    public static void main(String[] args) {
        Student student = new Student();

        // 3个参数分别代表：要修改的类，要修改的字段类型，要修改的字段名字
        AtomicReferenceFieldUpdater<Student, String> updater =
                AtomicReferenceFieldUpdater.newUpdater(Student.class, String.class, "name");

        // 第二个参数是当前对象的字段的值
        updater.compareAndSet(student, null, "Lisi");
        System.out.println("student = " + student);
    }
}

class Student {
    volatile String name;

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                '}';
    }
}

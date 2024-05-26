package org.example.designpattern;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

@Slf4j
public class ProducerAndConsumer {

    public static void main(String[] args) {
        MessageQueue messageQueue = new MessageQueue(2);

        // producer
        for (int i = 0; i < 3; i++) {
            int id = i;
            new Thread(() -> {
                try {
                    messageQueue.put(new Message(id, "value-" + id));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }, "Producer-" + i).start();
        }

        new Thread(() -> {
            try {
                messageQueue.get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, "consumer").start();
    }
}

@Slf4j
class MessageQueue {
    private final LinkedList<Message> list;
    private final int capacity;

    public MessageQueue(int capacity) {
        this.capacity = capacity;
        this.list = new LinkedList<>();
    }

    public Message get() throws InterruptedException {
        synchronized (this.list) {
            while (list.isEmpty()) {
                log.info("empty list");
                list.wait();
            }

            Message message = list.removeFirst();
            list.notifyAll();
            log.info("consumer message: {}", message);
            return message;
        }
    }

    public void put(Message message) throws InterruptedException {
        synchronized (this.list) {
            while (this.capacity == list.size()) {
                log.info("queue max size, can not put");
                list.wait();
            }

            list.addLast(message);
            log.info("put the message: {}", message);
            list.notifyAll();
        }
    }
}

final class Message {
    private final int id;
    private final Object message;

    public Message(int id, Object message) {
        this.id = id;
        this.message = message;
    }

    public Object getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", message=" + message +
                '}';
    }
}
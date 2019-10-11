package com.gagarin;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WaitAndNotify {
    public static void main(String[] args) {
        ConsumerProducer cp = new ConsumerProducer();

        Thread thread1 = new Thread(() -> {
            try {
                cp.produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                cp.consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread1.start();
        thread2.start();

    }
}

class ConsumerProducer {
    private Queue<Integer> queue = new LinkedList<>();
    private final int LIMIT = 10;
    private final Object lock = new Object();

    public void produce() throws InterruptedException {
        while (true) {
            synchronized (lock) {
                while (queue.size() >= LIMIT)
                    lock.wait();

                System.out.println("Putting new Integer into the Queue");
                System.out.println("");
                queue.offer(new Random().nextInt(100));
                lock.notify();
            }
        }
    }

    public void consume() throws InterruptedException {
        while (true) {
            synchronized (lock) {
                while (queue.size() <= 0)
                    lock.wait();

                int value = queue.poll();
                System.out.println("Getting value " + value + " from the queue");
                System.out.println("Queue size: " + queue.size());
                lock.notify();
            }

            Thread.sleep(1000);
        }
    }

}

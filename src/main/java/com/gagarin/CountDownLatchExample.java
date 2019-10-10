package com.gagarin;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchExample {
    public static void main(String[] args) throws InterruptedException {
        //создаём защёлку с обратным отсчётом
        CountDownLatch countDownLatch = new CountDownLatch(3);

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 3; i++)
            executorService.submit(new Processor(countDownLatch));

        //говорим что больше потоков не поступит
        executorService.shutdown();

        //ожидаем открытия защёлки
        countDownLatch.await();
        System.out.println("Latch has been opened, main thread is proceeding!");

    }
}

class Processor implements Runnable {

    private CountDownLatch countDownLatch;

    //передаём защёлку в Runnable, она потоко безопасна и потому не требует synchronized
    public Processor(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
        System.out.println("Start of the thread " + Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        countDownLatch.countDown();
    }
}

package com.gagarin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreExample {
    public static void main(String[] args) throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(200);
        Connection connection = Connection.getConnection();

        for (int i = 0; i < 200; i++) {
            executorService.submit(() -> {
                try {
                    connection.work();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        //Очень важно не забыть сказать свервису, что больше потоков не будет
        executorService.shutdown();

        //ожидание окончания работы сервиса
        executorService.awaitTermination(1, TimeUnit.DAYS);

        System.out.println(Connection.connectionsCount);
    }
}

class Connection {
    private static Connection connection;
    public static int connectionsCount;
    private Semaphore semaphore = new Semaphore(10);


    private Connection() {

    }

    public static synchronized Connection getConnection() {
        if (connection == null) {
            connection = new Connection();
        }
        return connection;
    }

    public void work() throws InterruptedException {
        semaphore.acquire();
        try {
            doWork();
        } finally {
            semaphore.release();
        }//т.к. во время doWork может возникнуть исключение, обязательно прпоисывать release() в блоке finally
    }

    public void doWork() throws InterruptedException {
        synchronized (this) {
            connectionsCount++;
            System.out.println(connectionsCount);
        }

        Thread.sleep(5000);

        synchronized (this) {
            connectionsCount--;
        }
    }
}


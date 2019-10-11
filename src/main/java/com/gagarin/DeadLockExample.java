package com.gagarin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class DeadLockExample {
    public static void main(String[] args) throws InterruptedException {
        Runner runner = new Runner();

        Thread thread1 = new Thread(() -> {
            runner.firstThread();
        });

        Thread thread2 = new Thread(() -> {
            runner.secondThread();
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        runner.finished();
    }
}

class Runner {
    private Account account1 = new Account();
    private Account account2 = new Account();


    // Set demonstration using HashSet Constructor
    Set<Integer> set = new HashSet<>(Arrays.asList(1, 2));


    public void firstThread() {
        Random random = new Random();

        synchronized (account1) {
            synchronized (account2) {
                for (int i = 0; i < 10000; i++) {
                    Account.transfer(account1, account2, random.nextInt(100));
                }
            }
        }
    }

    public void secondThread() {
        Random random = new Random();

        synchronized (account1) {
            synchronized (account2) {
                for (int i = 0; i < 10000; i++) {
                    Account.transfer(account2, account1, random.nextInt(100));
                }
            }
        }
    }

    public void finished() {
        System.out.println(account1.getBalance());
        System.out.println(account2.getBalance());
        System.out.println("Total balance " + (account1.getBalance() + account2.getBalance()));
    }
}

class Account {
    private int balance = 10000;

    public void deposit(int amount) {
        balance += amount;
    }

    public void withdraw(int amount) {
        balance -= amount;
    }

    public int getBalance() {
        return balance;
    }

    public static void transfer(Account acc1, Account acc2, int amount) {
        acc1.withdraw(amount);
        acc2.deposit(amount);
    }
}
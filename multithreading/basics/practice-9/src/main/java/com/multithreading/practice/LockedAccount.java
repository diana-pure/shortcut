package com.multithreading.practice;

import java.util.concurrent.locks.ReentrantLock;

public class LockedAccount {

    public int balance;
    public final ReentrantLock lock = new ReentrantLock();

    public LockedAccount(int initialBalance) {
        this.balance = initialBalance;
    }
}

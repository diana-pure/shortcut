package com.multithreading.practice;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Пул соединений на основе Semaphore.
 *
 * Реализуйте методы:
 * - Конструктор принимает int maxConnections
 * - acquire() — захватывает соединение (блокируется если пул заполнен)
 * - release() — освобождает соединение
 * - getActiveCount() — количество активных соединений
 * - getMaxConnections() — максимальное количество соединений
 *
 * Используйте Semaphore(maxConnections) и AtomicInteger для подсчёта активных.
 */
public class ConnectionPool {

    public ConnectionPool(int maxConnections) {
    }

    public void acquire() throws InterruptedException {
    }

    public void release() {
    }

    public int getActiveCount() {
        return 0;
    }

    public int getMaxConnections() {
        return 0;
    }
}

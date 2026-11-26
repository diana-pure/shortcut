package com.multithreading.practice;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Задача для инкремента общего счётчика (Runnable).
 *
 * Реализуйте:
 * - Конструктор принимает AtomicInteger counter
 * - run() — инкрементирует counter один раз
 */
public class CounterTask implements Runnable {

    public CounterTask(AtomicInteger counter) {
    }

    @Override
    public void run() {
    }
}

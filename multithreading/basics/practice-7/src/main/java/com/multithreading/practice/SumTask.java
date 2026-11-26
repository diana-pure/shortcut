package com.multithreading.practice;

import java.util.concurrent.Callable;

/**
 * Задача для вычисления суммы от 1 до n (Callable<Long>).
 *
 * Реализуйте:
 * - Конструктор принимает int n
 * - call() — возвращает сумму 1 + 2 + ... + n как Long
 * - При n < 0 бросает IllegalArgumentException
 */
public class SumTask implements Callable<Long> {

    public SumTask(int n) {
    }

    @Override
    public Long call() throws Exception {
        return 0L;
    }
}

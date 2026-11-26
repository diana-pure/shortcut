package com.multithreading.practice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Симулятор I/O нагрузки с использованием виртуальных потоков.
 *
 * Реализуйте методы:
 *
 * 1. simulateWithVirtualThreads(int requestCount, int delayMs) → long
 *    - Создайте requestCount задач, каждая симулирует I/O: Thread.sleep(delayMs)
 *    - Выполните через Executors.newVirtualThreadPerTaskExecutor()
 *    - Верните время выполнения в миллисекундах (System.currentTimeMillis())
 *
 * 2. simulateWithFixedPool(int requestCount, int delayMs, int poolSize) → long
 *    - Аналогично, но через Executors.newFixedThreadPool(poolSize)
 *    - Верните время выполнения
 *
 * Ключевое наблюдение: при requestCount >> poolSize виртуальные потоки
 * работают значительно быстрее, так как не блокируют OS-потоки при sleep.
 */
public class IoSimulator {

    public long simulateWithVirtualThreads(int requestCount, int delayMs) {
        return 0L;
    }

    public long simulateWithFixedPool(int requestCount, int delayMs, int poolSize) {
        return 0L;
    }
}

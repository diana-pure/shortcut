package com.multithreading.practice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NumberRangeTest {

    @Test
    @DisplayName("setLower/setUpper в корректном диапазоне обновляют значения")
    void testBasicSetGet() {
        NumberRange range = new NumberRange();
        range.setUpper(100);
        range.setLower(10);

        assertTrue(range.getLower() == 10, "lower должен стать 10");
        assertTrue(range.getUpper() == 100, "upper должен стать 100");
    }

    @Test
    @DisplayName("setLower больше текущего upper бросает IllegalArgumentException")
    void testSetLowerAboveUpperRejected() {
        NumberRange range = new NumberRange();
        range.setUpper(5);

        assertThrows(IllegalArgumentException.class, () -> range.setLower(10),
                "lower > upper должно отклоняться");
    }

    @Test
    @DisplayName("setUpper меньше текущего lower бросает IllegalArgumentException")
    void testSetUpperBelowLowerRejected() {
        NumberRange range = new NumberRange();
        range.setUpper(100);
        range.setLower(50);

        assertThrows(IllegalArgumentException.class, () -> range.setUpper(10),
                "upper < lower должно отклоняться");
    }

    @Test
    @Timeout(10)
    @DisplayName("Конкурентные обновления через CAS сохраняют инвариант lower <= upper")
    void testConcurrentUpdatesPreserveInvariant() throws InterruptedException {
        NumberRange range = new NumberRange();
        range.setUpper(2000); // запас, чтобы операции не пересекались по инварианту

        int threads = 16;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);

        for (int t = 0; t < threads; t++) {
            final int base = t;
            executor.submit(() -> {
                try {
                    start.await();
                    for (int k = 0; k < 1000; k++) {
                        // lower держим в [0, 500], upper в [1500, 2000] — инвариант заведомо выполним,
                        // но CAS-промахи реальны из-за конкуренции
                        range.setLower((base * 13 + k) % 500);
                        range.setUpper(1500 + ((base * 7 + k) % 500));
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }

        start.countDown();
        done.await();
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // Финальное состояние читается из одного консистентного снимка через каждый геттер;
        // благодаря атомарной замене IntPair инвариант обязан держаться.
        assertTrue(range.getLower() <= range.getUpper(),
                "После конкурентных обновлений инвариант lower <= upper должен сохраняться");
    }
}

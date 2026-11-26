package com.multithreading.practice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PerformanceScalabilityTest {

    @Test
    @Timeout(10)
    @DisplayName("ParallelArraySum: параллельная сумма даёт тот же результат, что последовательная")
    void testParallelSumCorrectness() throws InterruptedException {
        ParallelArraySum summer = new ParallelArraySum();
        long[] array = new long[100_000];
        for (int i = 0; i < array.length; i++) array[i] = i + 1;

        long sequential = summer.sequentialSum(array);
        long parallel4 = summer.parallelSum(array, 4);
        long parallel8 = summer.parallelSum(array, 8);

        assertEquals(sequential, parallel4, "4 потока должны дать тот же результат");
        assertEquals(sequential, parallel8, "8 потоков должны дать тот же результат");
    }

    @Test
    @Timeout(10)
    @DisplayName("ParallelArraySum: параллельная сумма массива [1..100] = 5050")
    void testParallelSumSmall() throws InterruptedException {
        ParallelArraySum summer = new ParallelArraySum();
        long[] array = new long[100];
        for (int i = 0; i < 100; i++) array[i] = i + 1;

        assertEquals(5050L, summer.parallelSum(array, 2));
        assertEquals(5050L, summer.parallelSum(array, 1));
    }

    @Test
    @Timeout(10)
    @DisplayName("StripedCounter: concurrent increment по 1000 раз из 10 потоков для одного ключа = 10000")
    void testStripedCounterConcurrency() throws InterruptedException {
        StripedCounter counter = new StripedCounter(8);
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10);

        for (int t = 0; t < 10; t++) {
            executor.submit(() -> {
                for (int i = 0; i < 1000; i++) counter.increment("sharedKey");
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();

        assertEquals(10_000L, counter.get("sharedKey"));
    }

    @Test
    @Timeout(10)
    @DisplayName("StripedCounter: разные ключи получают независимые счётчики")
    void testStripedCounterIndependence() throws InterruptedException {
        StripedCounter counter = new StripedCounter(4);
        ExecutorService executor = Executors.newFixedThreadPool(4);
        CountDownLatch latch = new CountDownLatch(4);

        String[] keys = {"alpha", "beta", "gamma", "delta"};
        for (String key : keys) {
            executor.submit(() -> {
                for (int i = 0; i < 500; i++) counter.increment(key);
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();

        for (String key : keys) {
            assertEquals(500L, counter.get(key), "Ключ " + key + " должен иметь счётчик 500");
        }
    }

    @Test
    @DisplayName("AmdahlCalculator: S=0.1 при 4 процессорах — ускорение ~3.08")
    void testAmdahlSpeedup() {
        double speedup = AmdahlCalculator.theoreticalSpeedup(0.1, 4);
        assertEquals(3.077, speedup, 0.001, "Закон Амдала: speedup при S=0.1, N=4");
    }

    @Test
    @DisplayName("AmdahlCalculator: максимальное ускорение при S=0.2 равно 5.0")
    void testAmdahlMaxSpeedup() {
        double max = AmdahlCalculator.maxSpeedup(0.2);
        assertEquals(5.0, max, 0.001, "При S=0.2 максимальное ускорение = 1/S = 5");
    }
}

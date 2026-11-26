package com.multithreading.practice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PoolShutdownTest {

    @Test
    @Timeout(10)
    @DisplayName("PoolShutdown: при щедром таймауте короткие задачи доигрывают, возвращается true")
    void testGracefulCompletion() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        AtomicInteger done = new AtomicInteger(0);

        for (int i = 0; i < 4; i++) {
            pool.execute(() -> {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                done.incrementAndGet();
            });
        }

        boolean graceful = PoolShutdown.shutdownGracefully(pool, 5, TimeUnit.SECONDS);

        assertTrue(graceful, "При достаточном таймауте завершение должно быть мягким (true)");
        assertEquals(4, done.get(), "Все принятые задачи должны успеть выполниться");
        assertTrue(pool.isTerminated(), "Пул должен быть полностью завершён");
    }

    @Test
    @Timeout(10)
    @DisplayName("PoolShutdown: задача дольше таймаута приводит к shutdownNow, возвращается false")
    void testForcefulShutdown() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(1);
        AtomicInteger interrupted = new AtomicInteger(0);

        pool.execute(() -> {
            try {
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                interrupted.incrementAndGet();
                Thread.currentThread().interrupt();
            }
        });

        boolean graceful = PoolShutdown.shutdownGracefully(pool, 200, TimeUnit.MILLISECONDS);

        assertFalse(graceful, "Затянувшаяся задача не должна уложиться в таймаут (false)");
        assertTrue(pool.isShutdown(), "После shutdownNow пул помечен как shutdown");
        assertEquals(1, interrupted.get(), "shutdownNow должен прервать выполняющуюся задачу");
        assertTrue(pool.awaitTermination(2, TimeUnit.SECONDS), "После прерывания пул должен завершиться");
    }

    @Test
    @Timeout(10)
    @DisplayName("PoolShutdown: после завершения новые задачи отклоняются")
    void testRejectsAfterShutdown() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(1);
        boolean graceful = PoolShutdown.shutdownGracefully(pool, 1, TimeUnit.SECONDS);

        assertTrue(graceful, "Пустой пул завершается мягко");
        assertTrue(pool.isShutdown(), "Пул должен быть в состоянии shutdown");
    }
}

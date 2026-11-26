package com.multithreading.practice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ThreadRunnableCallableTest {

    @Test
    @Timeout(10)
    @DisplayName("CounterTask (Runnable): 10 потоков × 1000 инкрементов = 10000")
    void testRunnableCounter() throws InterruptedException {
        AtomicInteger counter = new AtomicInteger(0);
        int threadCount = 10;
        int increments = 1000;

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < threadCount * increments; i++) {
            executor.submit(new CounterTask(counter));
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        assertEquals(threadCount * increments, counter.get());
    }

    @Test
    @Timeout(10)
    @DisplayName("SumTask (Callable<Long>): сумма 1..100 = 5050")
    void testCallableSum() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Long> future = executor.submit(new SumTask(100));

        assertEquals(5050L, future.get());
        executor.shutdown();
    }

    @Test
    @Timeout(10)
    @DisplayName("SumTask: несколько Callable параллельно — каждый считает свою сумму")
    void testMultipleCallables() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<Future<Long>> futures = new ArrayList<>();

        for (int n = 1; n <= 5; n++) {
            futures.add(executor.submit(new SumTask(n)));
        }

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        long[] expected = {1L, 3L, 6L, 10L, 15L};
        for (int i = 0; i < 5; i++) {
            try {
                assertEquals(expected[i], futures.get(i).get(), "SumTask(" + (i + 1) + ") должен вернуть " + expected[i]);
            } catch (ExecutionException e) {
                fail("Неожиданное исключение: " + e.getCause());
            }
        }
    }

    @Test
    @Timeout(10)
    @DisplayName("SumTask: исключение в Callable оборачивается в ExecutionException")
    void testCallableWithException() throws InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Long> future = executor.submit(new SumTask(-1));

        ExecutionException ex = assertThrows(ExecutionException.class, future::get);
        assertNotNull(ex.getCause(), "Причина исключения должна быть доступна через getCause()");
        executor.shutdown();
    }

    @Test
    @Timeout(5)
    @DisplayName("InterruptibleTask: поток корректно прерывается и устанавливает completed=true")
    void testInterruption() throws InterruptedException {
        AtomicBoolean completed = new AtomicBoolean(false);
        Thread thread = new Thread(new InterruptibleTask(completed));
        thread.start();
        Thread.sleep(50);
        thread.interrupt();
        thread.join(2000);

        assertFalse(thread.isAlive(), "Поток должен завершиться после прерывания");
        assertTrue(completed.get(), "completed должен быть true после завершения задачи");
    }
}

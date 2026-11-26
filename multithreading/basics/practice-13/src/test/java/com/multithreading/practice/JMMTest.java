package com.multithreading.practice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class JMMTest {

    @Test
    @Timeout(5)
    @DisplayName("StopFlag: volatile поле — поток останавливается после requestStop()")
    void testVolatileStopsThread() throws InterruptedException {
        StopFlag flag = new StopFlag();
        AtomicInteger iterations = new AtomicInteger(0);

        Thread worker = new Thread(() -> {
            while (!flag.isStopped()) {
                iterations.incrementAndGet();
            }
        });

        worker.start();
        Thread.sleep(50);
        flag.requestStop();
        worker.join(2000);

        assertFalse(worker.isAlive(), "Поток должен остановиться после requestStop()");
        assertTrue(iterations.get() > 0, "Поток должен выполнить хотя бы одну итерацию");
    }

    @Test
    @Timeout(5)
    @DisplayName("SafePublisher: volatile публикация — объект виден другому потоку корректно")
    void testVolatilePublication() throws InterruptedException, ExecutionException {
        SafePublisher publisher = new SafePublisher();
        SafePublisher.Config config = new SafePublisher.Config("test", 42);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(() -> publisher.publishWithVolatile(config));
        future.get();

        SafePublisher.Config read = publisher.getVolatileConfig();
        assertNotNull(read, "Опубликованный объект должен быть видим");
        assertEquals("test", read.name);
        assertEquals(42, read.version);
        executor.shutdown();
    }

    @Test
    @Timeout(5)
    @DisplayName("SafePublisher: synchronized публикация — объект виден другому потоку корректно")
    void testSynchronizedPublication() throws InterruptedException, ExecutionException {
        SafePublisher publisher = new SafePublisher();
        SafePublisher.Config config = new SafePublisher.Config("sync", 7);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<?> future = executor.submit(() -> publisher.publishWithSynchronized(config));
        future.get();

        SafePublisher.Config read = publisher.getSynchronizedConfig();
        assertNotNull(read);
        assertEquals("sync", read.name);
        executor.shutdown();
    }

    @Test
    @DisplayName("SafePublisher: eager initialization — объект создаётся при загрузке класса")
    void testEagerInitialization() {
        SafePublisher.Config config = new SafePublisher().getEagerConfig();
        assertNotNull(config, "Eager-инициализация должна создавать объект сразу");
        assertEquals("eager", config.name);
    }

    @Test
    @Timeout(10)
    @DisplayName("DoubleCheckedHolder: synchronized — 100 потоков получают один и тот же экземпляр")
    void testSynchronizedSingleton() throws InterruptedException {
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(100);
        DoubleCheckedHolder.ExpensiveObject[] results = new DoubleCheckedHolder.ExpensiveObject[100];
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 100; i++) {
            final int idx = i;
            executor.submit(() -> {
                try {
                    start.await();
                    results[idx] = DoubleCheckedHolder.getSynchronizedInstance();
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

        for (DoubleCheckedHolder.ExpensiveObject obj : results) {
            assertSame(results[0], obj, "Все потоки должны получить один объект (synchronized)");
        }
    }

    @Test
    @Timeout(10)
    @DisplayName("DoubleCheckedHolder: volatile DCL — 100 потоков получают один и тот же экземпляр")
    void testVolatileDCLSingleton() throws InterruptedException {
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(100);
        DoubleCheckedHolder.ExpensiveObject[] results = new DoubleCheckedHolder.ExpensiveObject[100];
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 100; i++) {
            final int idx = i;
            executor.submit(() -> {
                try {
                    start.await();
                    results[idx] = DoubleCheckedHolder.getVolatileDCL();
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

        for (DoubleCheckedHolder.ExpensiveObject obj : results) {
            assertSame(results[0], obj, "Все потоки должны получить один объект (volatile DCL)");
        }
    }

    @Test
    @Timeout(10)
    @DisplayName("DoubleCheckedHolder: Holder — 100 потоков получают один и тот же экземпляр")
    void testHolderSingleton() throws InterruptedException {
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(100);
        DoubleCheckedHolder.ExpensiveObject[] results = new DoubleCheckedHolder.ExpensiveObject[100];
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 100; i++) {
            final int idx = i;
            executor.submit(() -> {
                try {
                    start.await();
                    results[idx] = DoubleCheckedHolder.getHolderInstance();
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

        for (DoubleCheckedHolder.ExpensiveObject obj : results) {
            assertSame(results[0], obj, "Все потоки должны получить один объект (Holder)");
        }
    }
}

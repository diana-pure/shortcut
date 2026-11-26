package com.multithreading.practice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ConcurrentCollectionsTest {

    @Test
    @Timeout(10)
    @DisplayName("ConcurrentCache: потокобезопасная запись из 10 потоков — 100 уникальных ключей")
    void testConcurrentCachePutGet() throws InterruptedException {
        ConcurrentCache cache = new ConcurrentCache();
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(100);

        for (int i = 0; i < 100; i++) {
            final int id = i;
            executor.submit(() -> {
                cache.put("key" + id, id);
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();

        assertEquals(100, cache.size(), "Кэш должен содержать 100 элементов");
        assertEquals(42, cache.get("key42"), "Значение должно корректно читаться");
    }

    @Test
    @DisplayName("ConcurrentCache: computeIfAbsent не перезаписывает существующее значение")
    void testGetOrCompute() {
        ConcurrentCache cache = new ConcurrentCache();
        cache.put("key", 1);

        Object result = cache.getOrCompute("key", k -> 99);

        assertEquals(1, result, "getOrCompute не должен перезаписывать существующее значение");
    }

    @Test
    @DisplayName("ConcurrentCache: compute атомарно обновляет значение дважды")
    void testComputeValue() {
        ConcurrentCache cache = new ConcurrentCache();

        cache.computeValue("counter", (k, v) -> v == null ? 1 : v + 1);
        cache.computeValue("counter", (k, v) -> v == null ? 1 : v + 1);

        assertEquals(2, cache.get("counter"), "Значение должно быть увеличено дважды");
    }

    @Test
    @DisplayName("ConcurrentCache: merge суммирует три значения")
    void testMergeValue() {
        ConcurrentCache cache = new ConcurrentCache();

        cache.mergeValue("key", 10, Integer::sum);
        cache.mergeValue("key", 20, Integer::sum);
        cache.mergeValue("key", 30, Integer::sum);

        assertEquals(60, cache.get("key"), "Значения должны быть сложены");
    }

    @Test
    @DisplayName("ConcurrentCache: incrementCounter для двух независимых ключей")
    void testIncrementCounter() {
        ConcurrentCache cache = new ConcurrentCache();

        cache.incrementCounter("a");
        cache.incrementCounter("a");
        cache.incrementCounter("b");

        assertEquals(2, cache.getCounter("a").get());
        assertEquals(1, cache.getCounter("b").get());
    }

    @Test
    @Timeout(10)
    @DisplayName("ProducerConsumer: производитель и потребитель — ровно 20 элементов каждый")
    void testProducerConsumer() throws InterruptedException {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
        ProducerConsumer pc = new ProducerConsumer(queue);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch done = new CountDownLatch(1);

        executor.submit(() -> {
            try { pc.produce(20); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        executor.submit(() -> {
            try { pc.consume(20); done.countDown(); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });

        done.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        assertEquals(20, pc.getProducedCount());
        assertEquals(20, pc.getConsumedCount());
    }

    @Test
    @Timeout(10)
    @DisplayName("EventListenerRegistry: CopyOnWriteArrayList — слушатели вызваны из параллельных потоков")
    void testEventListenerRegistry() throws InterruptedException {
        EventListenerRegistry registry = new EventListenerRegistry();
        AtomicInteger receivedCount = new AtomicInteger(0);

        registry.addListener(event -> receivedCount.incrementAndGet());
        registry.addListener(event -> receivedCount.incrementAndGet());

        ExecutorService executor = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(5);

        for (int i = 0; i < 5; i++) {
            final int id = i;
            executor.submit(() -> {
                registry.fireEvent("event-" + id);
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();

        assertEquals(2, registry.getListenerCount());
        assertEquals(10, receivedCount.get(), "5 событий × 2 слушателя = 10 вызовов");
    }
}

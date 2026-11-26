package com.multithreading.practice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты к задаче-ловушке InventoryService.
 *
 * Наивная реализация через get()+put() (check-then-act) эти тесты НЕ проходит:
 * - testConcurrentAddStock ловит потерю обновлений (lost update);
 * - testConcurrentReserveNeverOversells ловит двойное списание и уход в минус.
 * Проходит только реализация на атомарных merge()/compute().
 */
class InventoryServiceTrapTest {

    @Test
    @Timeout(15)
    @DisplayName("addStock: 10 потоков × 10 000 прибавок — без потери обновлений ровно 100 000")
    void testConcurrentAddStock() throws InterruptedException {
        InventoryService service = new InventoryService();
        int threads = 10;
        int perThread = 10_000;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);

        for (int t = 0; t < threads; t++) {
            executor.submit(() -> {
                try {
                    start.await();
                    for (int i = 0; i < perThread; i++) {
                        service.addStock("widget", 1);
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

        assertEquals(threads * perThread, service.getStock("widget"),
                "Потеря обновлений: check-then-act (get+put) не атомарен — используйте merge()");
    }

    @Test
    @Timeout(15)
    @DisplayName("reserve: 2 000 потоков резервируют по 1 — продано ровно 1 000, остаток не уходит в минус")
    void testConcurrentReserveNeverOversells() throws InterruptedException {
        InventoryService service = new InventoryService();
        int available = 1_000;
        service.addStock("ticket", available);

        int attempts = 2_000;
        ExecutorService executor = Executors.newFixedThreadPool(16);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(attempts);
        AtomicInteger successful = new AtomicInteger(0);

        for (int i = 0; i < attempts; i++) {
            executor.submit(() -> {
                try {
                    start.await();
                    if (service.reserve("ticket", 1)) {
                        successful.incrementAndGet();
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

        assertEquals(available, successful.get(),
                "Должно успешно зарезервироваться ровно столько, сколько было в наличии");
        assertEquals(0, service.getStock("ticket"), "Остаток должен быть равен 0");
        assertTrue(service.getStock("ticket") >= 0, "Остаток никогда не должен уходить в минус");
    }
}

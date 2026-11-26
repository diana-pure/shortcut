package com.multithreading.practice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты к задаче PriorityTaskQueue (PriorityBlockingQueue).
 */
class PriorityTaskQueueTest {

    @Test
    @Timeout(5)
    @DisplayName("PriorityBlockingQueue: задачи извлекаются по приоритету, а не по порядку вставки")
    void testOrderByPriority() throws InterruptedException {
        PriorityTaskQueue queue = new PriorityTaskQueue();

        // Вставляем в произвольном порядке приоритетов
        queue.submit("low", 10);
        queue.submit("high", 1);
        queue.submit("medium", 5);

        assertEquals("high", queue.takeNext(), "Первой должна выйти задача с наименьшим priority");
        assertEquals("medium", queue.takeNext(), "Второй — со средним приоритетом");
        assertEquals("low", queue.takeNext(), "Последней — с наибольшим значением priority");
    }

    @Test
    @DisplayName("PriorityBlockingQueue: unbounded — submit не блокирует даже при множестве элементов без потребителя")
    void testUnboundedNeverBlocksOnSubmit() {
        PriorityTaskQueue queue = new PriorityTaskQueue();

        for (int i = 0; i < 10_000; i++) {
            queue.submit("task-" + i, i);
        }

        assertEquals(10_000, queue.size(), "Все элементы должны поместиться — очередь неограниченная");
    }

    @Test
    @Timeout(5)
    @DisplayName("PriorityBlockingQueue: takeNext блокируется на пустой очереди до появления элемента")
    void testTakeBlocksWhenEmpty() throws InterruptedException {
        PriorityTaskQueue queue = new PriorityTaskQueue();
        AtomicReference<String> taken = new AtomicReference<>();
        CountDownLatch consumerStarted = new CountDownLatch(1);

        Thread consumer = new Thread(() -> {
            try {
                consumerStarted.countDown();
                taken.set(queue.takeNext());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        consumer.start();

        consumerStarted.await();
        // Дать потребителю войти в take() на пустой очереди
        Thread.sleep(100);
        assertEquals(null, taken.get(), "Пока элемента нет, takeNext должен блокироваться");

        queue.submit("late", 1);
        consumer.join(2000);

        assertTrue(taken.get() != null, "После submit потребитель должен разблокироваться");
        assertEquals("late", taken.get());
    }
}

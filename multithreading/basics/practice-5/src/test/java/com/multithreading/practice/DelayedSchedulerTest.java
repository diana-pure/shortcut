package com.multithreading.practice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты к задаче DelayedScheduler (DelayQueue).
 */
class DelayedSchedulerTest {

    @Test
    @Timeout(5)
    @DisplayName("DelayQueue: задачи извлекаются в порядке готовности, а не вставки")
    void testOrderByReadiness() throws InterruptedException {
        DelayedScheduler scheduler = new DelayedScheduler();

        // Вставляем «позднюю» первой — порядок должен определяться задержкой, а не вставкой
        scheduler.schedule("later", 300);
        scheduler.schedule("sooner", 50);

        assertEquals("sooner", scheduler.take(), "Первой должна выйти задача с меньшей задержкой");
        assertEquals("later", scheduler.take(), "Второй — задача с большей задержкой");
    }

    @Test
    @DisplayName("DelayQueue: poll() возвращает null, пока задержка не истекла")
    void testPollReturnsNullBeforeDelay() {
        DelayedScheduler scheduler = new DelayedScheduler();
        scheduler.schedule("future", 5_000);

        assertNull(scheduler.poll(), "Задача ещё не готова — poll() должен вернуть null");
        assertEquals(1, scheduler.size(), "Неготовая задача остаётся в очереди");
    }

    @Test
    @Timeout(5)
    @DisplayName("DelayQueue: take() блокируется до истечения задержки")
    void testTakeBlocksUntilReady() throws InterruptedException {
        DelayedScheduler scheduler = new DelayedScheduler();
        scheduler.schedule("task", 150);

        long start = System.nanoTime();
        String name = scheduler.take();
        long elapsedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

        assertEquals("task", name);
        assertTrue(elapsedMs >= 120, "take() должен был блокироваться ~150 мс, прошло: " + elapsedMs + " мс");
    }
}

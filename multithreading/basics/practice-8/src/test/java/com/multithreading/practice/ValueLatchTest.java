package com.multithreading.practice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValueLatchTest {

    @Test
    @Timeout(10)
    @DisplayName("ValueLatch: до установки значения isSet() == false")
    void testNotSetInitially() {
        ValueLatch<String> latch = new ValueLatch<>();
        assertFalse(latch.isSet(), "Свежая защёлка не должна считаться установленной");
    }

    @Test
    @Timeout(10)
    @DisplayName("ValueLatch: первый setValue побеждает, последующие игнорируются")
    void testFirstWriterWins() throws InterruptedException {
        ValueLatch<String> latch = new ValueLatch<>();
        latch.setValue("first");
        latch.setValue("second");
        latch.setValue("third");

        assertTrue(latch.isSet(), "После setValue защёлка должна быть установлена");
        assertEquals("first", latch.getValue(), "Должно сохраниться первое установленное значение");
    }

    @Test
    @Timeout(10)
    @DisplayName("ValueLatch: getValue() блокирует поток, пока другой поток не установит значение")
    void testGetValueBlocksUntilSet() throws InterruptedException {
        ValueLatch<Integer> latch = new ValueLatch<>();
        AtomicInteger received = new AtomicInteger(-1);
        CountDownLatch waiterStarted = new CountDownLatch(1);

        Thread waiter = new Thread(() -> {
            waiterStarted.countDown();
            try {
                received.set(latch.getValue());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        waiter.start();

        assertTrue(waiterStarted.await(2, TimeUnit.SECONDS), "Поток-ожидатель должен стартовать");
        // Пока значение не установлено — getValue() блокирует, received остаётся -1
        Thread.sleep(100);
        assertEquals(-1, received.get(), "До setValue getValue() должен блокироваться");

        latch.setValue(42);
        waiter.join(2000);
        assertEquals(42, received.get(), "После setValue ожидатель должен получить значение");
    }

    @Test
    @Timeout(10)
    @DisplayName("ValueLatch: при гонке нескольких писателей устанавливается ровно одно значение")
    void testConcurrentWritersSingleValue() throws InterruptedException {
        ValueLatch<Integer> latch = new ValueLatch<>();
        int writers = 8;
        ExecutorService pool = Executors.newFixedThreadPool(writers);
        CountDownLatch start = new CountDownLatch(1);

        for (int i = 0; i < writers; i++) {
            int v = i;
            pool.execute(() -> {
                try {
                    start.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                latch.setValue(v);
            });
        }
        start.countDown();

        pool.shutdown();
        assertTrue(pool.awaitTermination(5, TimeUnit.SECONDS));

        int result = latch.getValue();
        assertTrue(result >= 0 && result < writers, "Значение должно быть от одного из писателей: " + result);
        // Значение стабильно — повторное чтение даёт то же самое
        assertEquals(result, latch.getValue(), "Установленное значение не должно меняться");
    }
}

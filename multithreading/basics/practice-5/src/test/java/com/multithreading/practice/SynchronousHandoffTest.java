package com.multithreading.practice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Тесты к задаче SynchronousHandoff (SynchronousQueue).
 */
class SynchronousHandoffTest {

    @Test
    @DisplayName("SynchronousQueue: tryHandoff возвращает false без ожидающего получателя, size всегда 0")
    void testTryHandoffFailsWithoutReceiver() {
        SynchronousHandoff handoff = new SynchronousHandoff();

        assertFalse(handoff.tryHandoff("item"),
                "Без ожидающего receive() передача невозможна — offer должен вернуть false");
        assertEquals(0, handoff.size(), "У SynchronousQueue нет буфера — size всегда 0");
    }

    @Test
    @Timeout(5)
    @DisplayName("SynchronousQueue: handoff блокируется до receive и передаёт элемент напрямую")
    void testHandoffMeetsReceiver() throws InterruptedException {
        SynchronousHandoff handoff = new SynchronousHandoff();
        AtomicReference<String> received = new AtomicReference<>();
        CountDownLatch done = new CountDownLatch(1);

        Thread producer = new Thread(() -> {
            try {
                handoff.handoff("payload");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        producer.start();

        Thread consumer = new Thread(() -> {
            try {
                received.set(handoff.receive());
                done.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        consumer.start();

        done.await();
        producer.join(2000);
        consumer.join(2000);

        assertEquals("payload", received.get(), "Элемент должен быть передан напрямую от producer к consumer");
        assertEquals(0, handoff.size());
    }
}

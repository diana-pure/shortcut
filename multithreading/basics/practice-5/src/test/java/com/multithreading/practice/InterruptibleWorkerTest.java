package com.multithreading.practice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Тесты к задаче InterruptibleWorker (обработка InterruptedException).
 */
class InterruptibleWorkerTest {

    @Test
    @Timeout(5)
    @DisplayName("InterruptedException: воркер завершается по interrupt() и восстанавливает флаг прерывания")
    void testWorkerStopsAndRestoresFlag() throws InterruptedException {
        LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        InterruptibleWorker worker = new InterruptibleWorker(queue);

        // Заранее кладём 3 элемента — их воркер успеет обработать
        queue.put(1);
        queue.put(2);
        queue.put(3);

        Thread t = new Thread(worker::runUntilInterrupted);
        t.start();

        // Ждём, пока воркер обработает всё и заблокируется на пустой очереди
        long deadline = System.nanoTime() + 2_000_000_000L;
        while (worker.getProcessedCount() < 3 && System.nanoTime() < deadline) {
            Thread.sleep(10);
        }
        assertEquals(3, worker.getProcessedCount(), "Воркер должен обработать все доступные элементы");

        // Прерываем — take() должен бросить InterruptedException и воркер должен выйти
        t.interrupt();
        t.join(2000);

        assertFalse(t.isAlive(), "Воркер должен завершиться после interrupt()");
        assertTrue(worker.wasInterruptFlagRestored(),
                "После InterruptedException флаг прерывания должен быть восстановлен (Thread.currentThread().interrupt())");
    }
}

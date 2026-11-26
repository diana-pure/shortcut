package com.multithreading.practice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeadlockDetectorTest {

    /**
     * Сценарий и проверка отсутствия дедлока, и его обнаружения собраны в один тест
     * намеренно: дедлокнутые daemon-потоки живут до конца JVM, поэтому проверять
     * «пусто» нужно ДО их создания, в гарантированном порядке.
     */
    @Test
    @Timeout(10)
    @DisplayName("DeadlockDetector: находит оба потока, попавших во взаимоблокировку, и пуст до неё")
    void testDetectsDeadlock() throws InterruptedException {
        // 1. До создания дедлока наших потоков среди дедлокнутых быть не должно.
        Set<String> before = DeadlockDetector.findDeadlockedThreadNames();
        assertFalse(before.contains("deadlock-victim-1") || before.contains("deadlock-victim-2"),
                "До создания взаимоблокировки детектор не должен видеть наши потоки");

        // 2. Два потока захватывают замки во встречном порядке → гарантированный дедлок.
        //    CyclicBarrier гарантирует, что оба УЖЕ удерживают первый замок,
        //    прежде чем каждый попытается захватить второй.
        Object lockA = new Object();
        Object lockB = new Object();
        CyclicBarrier bothHoldFirst = new CyclicBarrier(2);

        Thread t1 = new Thread(() -> {
            synchronized (lockA) {
                awaitQuietly(bothHoldFirst);
                synchronized (lockB) { /* недостижимо — дедлок */ }
            }
        }, "deadlock-victim-1");

        Thread t2 = new Thread(() -> {
            synchronized (lockB) {
                awaitQuietly(bothHoldFirst);
                synchronized (lockA) { /* недостижимо — дедлок */ }
            }
        }, "deadlock-victim-2");

        // daemon — чтобы вечно заблокированные потоки не мешали завершению JVM.
        t1.setDaemon(true);
        t2.setDaemon(true);
        t1.start();
        t2.start();

        // 3. Дедлок наступает не мгновенно — опрашиваем детектор до таймаута.
        Set<String> deadlocked = Set.of();
        long deadline = System.currentTimeMillis() + 5000;
        while (System.currentTimeMillis() < deadline) {
            deadlocked = DeadlockDetector.findDeadlockedThreadNames();
            if (deadlocked.contains("deadlock-victim-1") && deadlocked.contains("deadlock-victim-2")) {
                break;
            }
            Thread.sleep(50);
        }

        assertTrue(deadlocked.contains("deadlock-victim-1") && deadlocked.contains("deadlock-victim-2"),
                "Детектор должен найти оба потока во взаимоблокировке, найдено: " + deadlocked);
    }

    private static void awaitQuietly(CyclicBarrier barrier) {
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            Thread.currentThread().interrupt();
        }
    }
}

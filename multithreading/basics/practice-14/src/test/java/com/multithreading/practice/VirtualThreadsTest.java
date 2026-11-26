package com.multithreading.practice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class VirtualThreadsTest {

    @Test
    @Timeout(10)
    @DisplayName("VirtualThreadProcessor: 1000 задач выполняются через виртуальные потоки")
    void testExecuteWithVirtualThreads() throws InterruptedException {
        VirtualThreadProcessor processor = new VirtualThreadProcessor();
        List<Runnable> tasks = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger(0);

        for (int i = 0; i < 1000; i++) {
            tasks.add(() -> {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                counter.incrementAndGet();
            });
        }

        int completed = processor.executeWithVirtualThreads(tasks);

        assertEquals(1000, completed, "Все 1000 задач должны завершиться");
        assertEquals(1000, counter.get(), "Счётчик должен быть 1000");
    }

    @Test
    @Timeout(10)
    @DisplayName("VirtualThreadProcessor: executeAndCollect сохраняет порядок результатов")
    void testExecuteAndCollect() throws InterruptedException, ExecutionException {
        VirtualThreadProcessor processor = new VirtualThreadProcessor();
        List<Callable<Integer>> tasks = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            final int value = i;
            tasks.add(() -> value * 2);
        }

        List<Integer> results = processor.executeAndCollect(tasks);

        assertEquals(10, results.size());
        for (int i = 0; i < 10; i++) {
            assertEquals(i * 2, results.get(i), "Результат на позиции " + i + " должен быть " + (i * 2));
        }
    }

    @Test
    @Timeout(5)
    @DisplayName("Виртуальный поток: Thread.ofVirtual() создаёт виртуальный поток")
    void testVirtualThreadIsVirtual() throws InterruptedException {
        AtomicBoolean wasVirtual = new AtomicBoolean(false);
        VirtualThreadProcessor processor = new VirtualThreadProcessor();

        Thread vt = Thread.ofVirtual().start(() -> {
            wasVirtual.set(processor.isVirtualThread(Thread.currentThread()));
        });
        vt.join(2000);

        assertTrue(wasVirtual.get(), "Thread.ofVirtual() должен создавать виртуальный поток");
    }

    @Test
    @Timeout(5)
    @DisplayName("Платформенный поток: isVirtual() возвращает false для обычного потока")
    void testPlatformThreadIsNotVirtual() throws InterruptedException {
        VirtualThreadProcessor processor = new VirtualThreadProcessor();
        AtomicBoolean isVirtual = new AtomicBoolean(true);

        Thread pt = Thread.ofPlatform().start(() -> {
            isVirtual.set(processor.isVirtualThread(Thread.currentThread()));
        });
        pt.join(2000);

        assertFalse(isVirtual.get(), "Обычный поток не должен быть виртуальным");
    }

    @Test
    @Timeout(30)
    @DisplayName("IoSimulator: виртуальные потоки быстрее фиксированного пула при I/O-нагрузке")
    void testVirtualThreadsFasterThanFixedPool() {
        IoSimulator simulator = new IoSimulator();
        int requests = 200;
        int delayMs = 50;
        int poolSize = 10;

        long virtualTime = simulator.simulateWithVirtualThreads(requests, delayMs);
        long fixedTime = simulator.simulateWithFixedPool(requests, delayMs, poolSize);

        assertTrue(virtualTime < fixedTime,
            "Виртуальные потоки должны быть быстрее (virtual=" + virtualTime + "ms, fixed=" + fixedTime + "ms)");
    }
}

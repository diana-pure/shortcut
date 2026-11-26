package com.multithreading.practice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Тесты к главе 11 «Тестирование параллельных программ». Демонстрируют сигнатурные техники
 * главы на сквозном примере {@link BoundedBuffer}:
 *  - последовательные модульные тесты (1.1);
 *  - тест блокирующей операции через interrupt + join(timeout) + isAlive (1.2);
 *  - тест безопасности через коммутативную контрольную сумму на xorShift и
 *    CyclicBarrier(N*2+1) как «стартовый выстрел» (1.3);
 *  - тест управления ресурсами / отсутствия утечки памяти (1.4);
 *  - тест производительности через BarrierTimer (2.1).
 *
 * Тесты НЕ изменять — реализуйте BoundedBuffer и BarrierTimer так, чтобы они проходили.
 */
class ConcurrentTestingTest {

    private static final long LOCKUP_DETECT_TIMEOUT_MS = 200;

    /**
     * Псевдослучайная функция среднего качества (см. раздел 1.3). Значения неугадываемы для
     * JIT, поэтому контрольную сумму нельзя предвычислить и выкинуть как мёртвый код —
     * в отличие от последовательных 0,1,2,3...
     */
    private static int xorShift(int y) {
        y ^= (y << 6);
        y ^= (y >>> 21);
        y ^= (y << 7);
        return y;
    }

    @Test
    @Timeout(10)
    @DisplayName("1.1 Только что созданный буфер пуст и не полон")
    void testIsEmptyWhenConstructed() {
        BoundedBuffer<Integer> bb = new BoundedBuffer<>(10);
        assertTrue(bb.isEmpty(), "новый буфер должен быть пуст");
        assertFalse(bb.isFull(), "новый буфер не должен быть полон");
    }

    @Test
    @Timeout(10)
    @DisplayName("1.1 После N вставок буфер полон и не пуст")
    void testIsFullAfterPuts() throws InterruptedException {
        BoundedBuffer<Integer> bb = new BoundedBuffer<>(10);
        for (int i = 0; i < 10; i++) {
            bb.put(i);
        }
        assertTrue(bb.isFull(), "после заполнения буфер должен быть полон");
        assertFalse(bb.isEmpty(), "полный буфер не пуст");
    }

    @Test
    @Timeout(10)
    @DisplayName("1.2 take() на пустом буфере блокируется и реагирует на прерывание")
    void testTakeBlocksWhenEmpty() throws InterruptedException {
        final BoundedBuffer<Integer> bb = new BoundedBuffer<>(10);
        Thread taker = new Thread(() -> {
            try {
                bb.take();
                fail("take() на пустом буфере не должен возвращаться без прерывания");
            } catch (InterruptedException success) {
                // ожидаемо: блокировка снята через interrupt()
            }
        });

        taker.start();
        Thread.sleep(LOCKUP_DETECT_TIMEOUT_MS);
        assertTrue(taker.isAlive(), "поток должен оставаться заблокированным на пустом буфере");

        taker.interrupt();
        taker.join(LOCKUP_DETECT_TIMEOUT_MS);
        assertFalse(taker.isAlive(), "после interrupt() поток должен завершиться (бросить InterruptedException)");
    }

    @RepeatedTest(5)
    @Timeout(30)
    @DisplayName("1.3 Safety: контрольные суммы put/take совпадают при конкурентном доступе")
    void testPutTakeChecksum() throws Exception {
        ExecutorService pool = Executors.newCachedThreadPool();
        try {
            PutTakeHarness harness = new PutTakeHarness(10, 10, 10_000, pool);
            harness.run();
            assertEquals(harness.putSum.get(), harness.takeSum.get(),
                    "суммы отправленных и принятых элементов должны совпасть");
        } finally {
            pool.shutdown();
        }
    }

    @Test
    @Timeout(30)
    @DisplayName("1.4 Управление ресурсами: take() не удерживает ссылку (нет утечки памяти)")
    void testLeak() throws InterruptedException {
        final int capacity = 200;
        BoundedBuffer<Big> bb = new BoundedBuffer<>(capacity);

        long baseline = usedMemory();
        for (int i = 0; i < capacity; i++) {
            bb.put(new Big());
        }
        for (int i = 0; i < capacity; i++) {
            bb.take();
        }

        long retained = usedMemory() - baseline;
        // Один Big ~ 80 КБ; capacity объектов ~ 16 МБ. Если doExtract не обнуляет ссылку,
        // массив удержит их все. Порог 8 МБ надёжно разделяет «обнулил» и «забыл обнулить».
        assertTrue(retained < 8L * 1024 * 1024,
                "после take() извлечённые объекты должны быть доступны для GC; удержано байт: " + retained);
    }

    @Test
    @Timeout(30)
    @DisplayName("2.1 Производительность: BarrierTimer измеряет throughput параллельной фазы")
    void testThroughput() throws Exception {
        ExecutorService pool = Executors.newCachedThreadPool();
        try {
            PutTakeHarness harness = new PutTakeHarness(10, 10, 10_000, pool);
            harness.run();
            // функциональная проверка корректности — измеряем производительность работающего кода
            assertEquals(harness.putSum.get(), harness.takeSum.get(), "throughput имеет смысл только для корректного кода");
            long nsPerItem = harness.nsPerItem();
            assertTrue(nsPerItem > 0, "BarrierTimer должен измерить положительное время на элемент");
            System.out.println("Throughput: " + nsPerItem + " ns/item");
        } finally {
            pool.shutdown();
        }
    }

    private static long usedMemory() {
        Runtime rt = Runtime.getRuntime();
        for (int i = 0; i < 3; i++) {
            rt.gc();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return rt.totalMemory() - rt.freeMemory();
    }

    /** Крупный объект (~80 КБ) для теста утечки памяти. */
    static final class Big {
        final double[] data = new double[10_000];
    }

    /**
     * Драйвер PutTakeTest из раздела 1.3: N пар производитель/потребитель, контрольная сумма
     * на xorShift, CyclicBarrier(N*2+1) синхронизирует старт и финиш всех потоков и драйвера.
     */
    private final class PutTakeHarness {

        private final BoundedBuffer<Integer> bb;
        private final AtomicInteger putSum = new AtomicInteger(0);
        private final AtomicInteger takeSum = new AtomicInteger(0);
        private final BarrierTimer timer = new BarrierTimer();
        private final CyclicBarrier barrier;
        private final ExecutorService pool;
        private final int nPairs;
        private final int nTrials;

        PutTakeHarness(int capacity, int nPairs, int nTrials, ExecutorService pool) {
            this.bb = new BoundedBuffer<>(capacity);
            this.nPairs = nPairs;
            this.nTrials = nTrials;
            this.pool = pool;
            this.barrier = new CyclicBarrier(nPairs * 2 + 1, timer);
        }

        void run() throws Exception {
            timer.clear();
            for (int i = 0; i < nPairs; i++) {
                pool.execute(new Producer());
                pool.execute(new Consumer());
            }
            barrier.await(); // стартовый выстрел: ждём готовности всех потоков
            barrier.await(); // ждём завершения всех потоков
        }

        long nsPerItem() {
            return timer.getTime() / (nPairs * (long) nTrials);
        }

        final class Producer implements Runnable {
            @Override
            public void run() {
                try {
                    int seed = this.hashCode() ^ (int) System.nanoTime();
                    int sum = 0;
                    barrier.await();
                    for (int i = nTrials; i > 0; --i) {
                        bb.put(seed);
                        sum += seed;
                        seed = xorShift(seed);
                    }
                    putSum.getAndAdd(sum);
                    barrier.await();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        final class Consumer implements Runnable {
            @Override
            public void run() {
                try {
                    barrier.await();
                    int sum = 0;
                    for (int i = nTrials; i > 0; --i) {
                        sum += bb.take();
                    }
                    takeSum.getAndAdd(sum);
                    barrier.await();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

package com.multithreading.practice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class SynchronizersTest {

    @Test
    @Timeout(10)
    @DisplayName("RaceCoordinator: все участники стартуют после сигнала, потом регистрируют финиш")
    void testRaceCoordinatorStartAndFinish() throws InterruptedException {
        int count = 5;
        RaceCoordinator coordinator = new RaceCoordinator(count);
        ExecutorService executor = Executors.newFixedThreadPool(count);
        AtomicInteger started = new AtomicInteger(0);

        for (int i = 0; i < count; i++) {
            executor.submit(() -> {
                try {
                    coordinator.readyUp();
                    coordinator.awaitStart();
                    started.incrementAndGet();
                    coordinator.finish();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        coordinator.startRace();
        coordinator.awaitAllFinish();
        executor.shutdown();

        assertEquals(count, started.get(), "Все участники должны стартовать");
    }

    @Test
    @Timeout(10)
    @DisplayName("ConnectionPool: Semaphore ограничивает одновременный доступ тремя потоками")
    void testConnectionPoolLimit() throws InterruptedException {
        ConnectionPool pool = new ConnectionPool(3);
        AtomicInteger maxSimultaneous = new AtomicInteger(0);
        AtomicInteger current = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(10);

        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                try {
                    pool.acquire();
                    int active = current.incrementAndGet();
                    maxSimultaneous.updateAndGet(max -> Math.max(max, active));
                    assertTrue(pool.getActiveCount() <= 3, "Активных не должно быть > 3");
                    Thread.sleep(50);
                    current.decrementAndGet();
                    pool.release();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        assertTrue(maxSimultaneous.get() <= 3, "Максимум одновременных соединений не должен превышать 3");
        assertEquals(0, pool.getActiveCount(), "После завершения все соединения должны быть освобождены");
    }

    @Test
    @Timeout(10)
    @DisplayName("ConnectionPool: getMaxConnections возвращает значение из конструктора")
    void testConnectionPoolCapacity() {
        ConnectionPool pool = new ConnectionPool(5);
        assertEquals(5, pool.getMaxConnections());
        assertEquals(0, pool.getActiveCount());
    }

    @Test
    @Timeout(10)
    @DisplayName("PhaseProcessor: CyclicBarrier синхронизирует 3 фазы из 4 потоков")
    void testPhaseProcessor() throws InterruptedException, BrokenBarrierException {
        int threads = 4;
        int phases = 3;
        PhaseProcessor processor = new PhaseProcessor(threads, phases);
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        AtomicInteger actionCount = new AtomicInteger(0);
        CountDownLatch done = new CountDownLatch(threads);

        for (int t = 0; t < threads; t++) {
            final int threadIdx = t;
            executor.submit(() -> {
                try {
                    for (int p = 0; p < phases; p++) {
                        processor.runPhase(threadIdx, () -> actionCount.incrementAndGet());
                    }
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }

        done.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        assertEquals(threads * phases, actionCount.get(), "Каждый поток должен выполнить каждую фазу");
        assertEquals(phases, processor.getCompletedPhases(), "Должно быть завершено 3 фазы");
    }

    @Test
    @Timeout(10)
    @DisplayName("CountDownLatch: стартовый пистолет — все потоки ждут открытия защёлки")
    void testCountDownLatchStartGun() throws InterruptedException {
        CountDownLatch startGun = new CountDownLatch(1);
        AtomicInteger readyCount = new AtomicInteger(0);
        ExecutorService executor = Executors.newFixedThreadPool(5);
        CountDownLatch allDone = new CountDownLatch(5);

        for (int i = 0; i < 5; i++) {
            executor.submit(() -> {
                try {
                    startGun.await();
                    readyCount.incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    allDone.countDown();
                }
            });
        }

        Thread.sleep(100);
        assertEquals(0, readyCount.get(), "До старта никто не должен выполниться");

        startGun.countDown();
        allDone.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        assertEquals(5, readyCount.get(), "После старта все потоки должны выполниться");
    }

    @Test
    @Timeout(10)
    @DisplayName("MultiPhaseTask: Phaser синхронизирует 3 фазы при фиксированном числе участников")
    void testPhaserMultiPhase() throws InterruptedException {
        int parties = 4;
        int phases = 3;
        MultiPhaseTask task = new MultiPhaseTask(parties);
        ExecutorService executor = Executors.newFixedThreadPool(parties);
        AtomicInteger work = new AtomicInteger(0);
        CountDownLatch done = new CountDownLatch(parties);

        for (int t = 0; t < parties; t++) {
            executor.submit(() -> {
                try {
                    for (int p = 0; p < phases; p++) {
                        work.incrementAndGet();
                        task.arriveAndAwait();
                    }
                } finally {
                    done.countDown();
                }
            });
        }

        assertTrue(done.await(5, TimeUnit.SECONDS), "Все участники должны пройти все фазы");
        executor.shutdown();

        assertEquals(parties * phases, work.get(), "Каждый участник должен выполнить каждую фазу");
        assertEquals(phases, task.getCompletedPhases(), "Должно быть завершено 3 фазы");
    }

    @Test
    @Timeout(10)
    @DisplayName("MultiPhaseTask: register() добавляет участников, arriveAndDeregister() удаляет и терминирует фазер")
    void testPhaserDynamicMembership() throws InterruptedException {
        MultiPhaseTask task = new MultiPhaseTask(0);

        // Динамически регистрируем 3 участников до старта потоков
        for (int i = 0; i < 3; i++) {
            task.register();
        }
        assertEquals(3, task.getRegisteredParties(), "После register() должно быть 3 участника");

        CountDownLatch done = new CountDownLatch(3);
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                task.arriveAndDeregister();
                done.countDown();
            }).start();
        }

        assertTrue(done.await(5, TimeUnit.SECONDS), "Все участники должны покинуть фазер");

        assertEquals(0, task.getRegisteredParties(), "После выхода всех участников должно остаться 0");
        assertEquals(1, task.getCompletedPhases(), "Фаза должна продвинуться один раз при сходе всех участников");
    }

    @Test
    @Timeout(10)
    @DisplayName("DataExchanger: два потока обмениваются объектами через Exchanger")
    void testExchangerSwapsValues() throws InterruptedException {
        DataExchanger<String> exchanger = new DataExchanger<>();
        AtomicReference<String> received1 = new AtomicReference<>();
        AtomicReference<String> received2 = new AtomicReference<>();
        CountDownLatch done = new CountDownLatch(2);

        Thread t1 = new Thread(() -> {
            try {
                received1.set(exchanger.exchange("A"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                done.countDown();
            }
        });
        Thread t2 = new Thread(() -> {
            try {
                received2.set(exchanger.exchange("B"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                done.countDown();
            }
        });

        t1.start();
        t2.start();

        assertTrue(done.await(5, TimeUnit.SECONDS), "Обмен должен завершиться");

        assertEquals("B", received1.get(), "Первый поток должен получить объект второго");
        assertEquals("A", received2.get(), "Второй поток должен получить объект первого");
    }

    @Test
    @Timeout(10)
    @DisplayName("DataExchanger: exchange с таймаутом бросает TimeoutException без партнёра")
    void testExchangerTimeout() {
        DataExchanger<String> exchanger = new DataExchanger<>();

        assertThrows(TimeoutException.class,
                () -> exchanger.exchange("solo", 200, TimeUnit.MILLISECONDS),
                "Без партнёра exchange с таймаутом должен бросить TimeoutException");
    }
}

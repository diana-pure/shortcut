package com.multithreading.practice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ThreadPoolsTest {

    // ===== Задание 1: BackPressureExecutor =====

    @Test
    @Timeout(10)
    @DisplayName("BackPressureExecutor: при переполнении задачи не теряются (CallerRunsPolicy выполняет все)")
    void testNoTasksLost() throws InterruptedException {
        ThreadPoolExecutor pool = BackPressureExecutor.create(1, 1);
        assertNotNull(pool, "create() должен вернуть ThreadPoolExecutor");

        AtomicInteger done = new AtomicInteger(0);
        for (int i = 0; i < 8; i++) {
            pool.execute(() -> {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                done.incrementAndGet();
            });
        }

        pool.shutdown();
        assertTrue(pool.awaitTermination(5, TimeUnit.SECONDS), "Пул должен завершиться");
        assertEquals(8, done.get(), "Все 8 задач должны выполниться — CallerRunsPolicy ничего не теряет");
    }

    @Test
    @Timeout(10)
    @DisplayName("BackPressureExecutor: при переполнении задача выполняется в вызывающем потоке (back pressure)")
    void testCallerRunsBackPressure() throws InterruptedException {
        ThreadPoolExecutor pool = BackPressureExecutor.create(1, 1);
        assertNotNull(pool, "create() должен вернуть ThreadPoolExecutor");

        String callerThreadName = Thread.currentThread().getName();
        Set<String> executingThreads = ConcurrentHashMap.newKeySet();

        // 1 поток + очередь на 1 → при 6 задачах часть попадёт под CallerRunsPolicy
        for (int i = 0; i < 6; i++) {
            pool.execute(() -> {
                executingThreads.add(Thread.currentThread().getName());
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        pool.shutdown();
        assertTrue(pool.awaitTermination(5, TimeUnit.SECONDS), "Пул должен завершиться");
        assertTrue(executingThreads.contains(callerThreadName),
                "Хотя бы одна задача должна выполниться в вызывающем потоке (" + callerThreadName + "); "
                        + "фактически выполняли: " + executingThreads);
    }

    // ===== Задание 2: TimingThreadPool =====

    @Test
    @Timeout(10)
    @DisplayName("TimingThreadPool: считает число задач и среднее время через хуки before/afterExecute")
    void testTimingHooks() throws InterruptedException {
        TimingThreadPool pool = new TimingThreadPool(3);

        for (int i = 0; i < 5; i++) {
            pool.execute(() -> {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        pool.shutdown();
        assertTrue(pool.awaitTermination(5, TimeUnit.SECONDS), "Пул должен завершиться");

        assertEquals(5, pool.getMeasuredTaskCount(), "Должно быть учтено 5 задач");
        double avg = pool.getAverageTaskTimeMillis();
        assertTrue(avg >= 40 && avg <= 250,
                "Среднее время задачи (~50 мс) должно быть в диапазоне [40; 250], фактически: " + avg);
    }

    // ===== Задание 3: NamedThreadFactory =====

    @Test
    @Timeout(10)
    @DisplayName("NamedThreadFactory: имена потоков нумеруются с 1, daemon-статус выставляется")
    void testThreadNamingAndDaemon() {
        NamedThreadFactory factory = new NamedThreadFactory("worker", true, (t, e) -> {});

        Thread t1 = factory.newThread(() -> {});
        Thread t2 = factory.newThread(() -> {});
        Thread t3 = factory.newThread(() -> {});

        assertNotNull(t1, "newThread() не должен возвращать null");
        assertEquals("worker-1", t1.getName());
        assertEquals("worker-2", t2.getName());
        assertEquals("worker-3", t3.getName());
        assertTrue(t1.isDaemon(), "Поток должен быть daemon");
        assertTrue(t2.isDaemon(), "Поток должен быть daemon");
    }

    @Test
    @Timeout(10)
    @DisplayName("NamedThreadFactory: UncaughtExceptionHandler ловит исключение из упавшего потока")
    void testUncaughtExceptionHandler() throws InterruptedException {
        AtomicReference<Throwable> caught = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);

        NamedThreadFactory factory = new NamedThreadFactory("failing", false, (t, e) -> {
            caught.set(e);
            latch.countDown();
        });

        Thread t = factory.newThread(() -> {
            throw new IllegalStateException("сбой задачи");
        });
        assertNotNull(t, "newThread() не должен возвращать null");
        t.start();

        assertTrue(latch.await(2, TimeUnit.SECONDS), "Handler должен быть вызван");
        assertNotNull(caught.get(), "Handler должен получить исключение");
        assertEquals("сбой задачи", caught.get().getMessage());
    }

    // ===== Задание 4: BoundedExecutor =====

    @Test
    @Timeout(10)
    @DisplayName("BoundedExecutor: семафор ограничивает фактический параллелизм границей bound")
    void testBoundedConcurrency() throws InterruptedException {
        ExecutorService backing = Executors.newFixedThreadPool(10);
        BoundedExecutor bounded = new BoundedExecutor(backing, 2);

        AtomicInteger concurrent = new AtomicInteger(0);
        AtomicInteger maxConcurrent = new AtomicInteger(0);
        CountDownLatch allDone = new CountDownLatch(6);

        for (int i = 0; i < 6; i++) {
            bounded.submitTask(() -> {
                int now = concurrent.incrementAndGet();
                maxConcurrent.accumulateAndGet(now, Math::max);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    concurrent.decrementAndGet();
                    allDone.countDown();
                }
            });
        }

        assertTrue(allDone.await(5, TimeUnit.SECONDS), "Все 6 задач должны выполниться");
        assertTrue(maxConcurrent.get() <= 2,
                "Несмотря на пул из 10 потоков, семафор (bound=2) должен ограничить параллелизм до 2, "
                        + "фактический максимум: " + maxConcurrent.get());
        assertTrue(maxConcurrent.get() >= 1, "Хотя бы одна задача должна выполниться");

        backing.shutdown();
        assertTrue(backing.awaitTermination(5, TimeUnit.SECONDS));
    }
}

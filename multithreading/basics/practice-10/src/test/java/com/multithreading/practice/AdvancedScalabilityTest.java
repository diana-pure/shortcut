package com.multithreading.practice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdvancedScalabilityTest {

    // ---------- ScopedLockCache: сужение области блокировки ----------

    @Test
    @DisplayName("ScopedLockCache: значение вычисляется один раз и затем берётся из кэша")
    void testScopedLockCacheCachesValue() {
        ScopedLockCache cache = new ScopedLockCache();
        AtomicInteger computeCalls = new AtomicInteger();
        Function<String, String> compute = key -> {
            computeCalls.incrementAndGet();
            return key.toUpperCase();
        };

        assertEquals("X", cache.getOrCompute("x", compute));
        assertEquals("X", cache.getOrCompute("x", compute));
        assertEquals(1, computeCalls.get(), "повторный вызов должен брать значение из кэша, не вычисляя заново");
    }

    @Test
    @Timeout(5)
    @DisplayName("ScopedLockCache: дорогое вычисление идёт вне блокировки — два потока входят в него параллельно")
    void testScopedLockCacheComputesOutsideLock() throws InterruptedException {
        ScopedLockCache cache = new ScopedLockCache();
        // barrier на 2 участников: соберётся, только если оба потока одновременно
        // находятся внутри expensiveCompute. Если бы вычисление шло под общей
        // блокировкой this, второй поток ждал бы первого — barrier не собрался бы за timeout.
        CyclicBarrier barrier = new CyclicBarrier(2);
        Function<String, String> compute = key -> {
            try {
                barrier.await(3, TimeUnit.SECONDS);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return key + "-value";
        };

        Thread t1 = new Thread(() -> cache.getOrCompute("a", compute));
        Thread t2 = new Thread(() -> cache.getOrCompute("b", compute));
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        // если параллельный вход состоялся, оба значения вычислены и закэшированы
        assertEquals("a-value", cache.getOrCompute("a", k -> {
            throw new IllegalStateException("значение должно уже быть в кэше");
        }));
        assertEquals("b-value", cache.getOrCompute("b", k -> {
            throw new IllegalStateException("значение должно уже быть в кэше");
        }));
    }

    // ---------- SplitLockServerStatus: разделение блокировок ----------

    @Test
    @Timeout(5)
    @DisplayName("SplitLockServerStatus: блокировки users и queries независимы — удержание одной не мешает другой")
    void testSplitLocksAreIndependent() throws InterruptedException {
        SplitLockServerStatus status = new SplitLockServerStatus();
        CountDownLatch queryDone = new CountDownLatch(1);

        synchronized (status.users) {              // держим блокировку users
            Thread t = new Thread(() -> {
                status.addQuery("q1");             // работает с queries — другая блокировка
                queryDone.countDown();
            });
            t.start();
            // при общей блокировке addQuery ждал бы освобождения users и не успел бы
            boolean completed = queryDone.await(2, TimeUnit.SECONDS);
            assertTrue(completed, "addQuery должен пройти, пока удерживается блокировка users");
            t.join();
        }

        assertEquals(1, status.queryCount());
    }

    @Test
    @Timeout(10)
    @DisplayName("SplitLockServerStatus: конкурентные добавления в users и queries не теряются")
    void testSplitLockConcurrentAdds() throws InterruptedException {
        SplitLockServerStatus status = new SplitLockServerStatus();
        int n = 2000;
        ExecutorService executor = Executors.newFixedThreadPool(8);
        CountDownLatch latch = new CountDownLatch(2);

        executor.submit(() -> {
            for (int i = 0; i < n; i++) status.addUser("u" + i);
            latch.countDown();
        });
        executor.submit(() -> {
            for (int i = 0; i < n; i++) status.addQuery("q" + i);
            latch.countDown();
        });

        latch.await();
        executor.shutdown();

        assertEquals(n, status.userCount());
        assertEquals(n, status.queryCount());
    }

    // ---------- LongAdderCounter: устранение горячего поля ----------

    @Test
    @Timeout(10)
    @DisplayName("LongAdderCounter: 16 потоков по 100000 инкрементов дают 1600000 без потерь")
    void testLongAdderConcurrency() throws InterruptedException {
        LongAdderCounter counter = new LongAdderCounter();
        int threads = 16;
        int perThread = 100_000;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(threads);

        for (int t = 0; t < threads; t++) {
            executor.submit(() -> {
                for (int i = 0; i < perThread; i++) counter.increment();
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();

        assertEquals((long) threads * perThread, counter.sum());
    }

    @Test
    @DisplayName("LongAdderCounter: add накапливает значения, reset обнуляет")
    void testLongAdderAddAndReset() {
        LongAdderCounter counter = new LongAdderCounter();
        counter.add(10);
        counter.add(5);
        counter.increment();
        assertEquals(16, counter.sum());

        counter.reset();
        assertEquals(0, counter.sum());
    }

    // ---------- ReadWriteCache: ReadWriteLock вместо монопольной блокировки ----------

    @Test
    @DisplayName("ReadWriteCache: put сохраняет значение, get его читает")
    void testReadWriteCacheBasic() {
        ReadWriteCache cache = new ReadWriteCache();
        cache.put("url", "https://example.com");
        assertEquals("https://example.com", cache.get("url"));
        assertEquals(1, cache.size());
    }

    @Test
    @Timeout(5)
    @DisplayName("ReadWriteCache: читатели работают параллельно — три потока входят в readSnapshot одновременно")
    void testReadWriteCacheConcurrentReaders() throws InterruptedException {
        ReadWriteCache cache = new ReadWriteCache();
        cache.put("k", "v");
        // barrier на 3 участника соберётся, только если три читателя одновременно
        // находятся внутри readLock. С монопольной блокировкой они бы сериализовались.
        CyclicBarrier barrier = new CyclicBarrier(3);
        CountDownLatch done = new CountDownLatch(3);
        String[] results = new String[3];

        for (int i = 0; i < 3; i++) {
            final int idx = i;
            new Thread(() -> {
                results[idx] = cache.readSnapshot(map -> {
                    try {
                        barrier.await(3, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    return map.get("k");
                });
                done.countDown();
            }).start();
        }

        assertTrue(done.await(4, TimeUnit.SECONDS), "три читателя должны войти в readLock параллельно");
        for (String r : results) {
            assertEquals("v", r, "readSnapshot должен выполнить reader под readLock и вернуть значение");
        }
    }

    @Test
    @Timeout(10)
    @DisplayName("ReadWriteCache: конкурентные читатели и писатели не теряют записи")
    void testReadWriteCacheConcurrentReadWrite() throws InterruptedException {
        ReadWriteCache cache = new ReadWriteCache();
        int writers = 4;
        int perWriter = 500;
        ExecutorService executor = Executors.newFixedThreadPool(writers + 4);
        CountDownLatch latch = new CountDownLatch(writers + 4);

        for (int w = 0; w < writers; w++) {
            int base = w * perWriter;
            executor.submit(() -> {
                for (int i = 0; i < perWriter; i++) cache.put("key" + (base + i), "v");
                latch.countDown();
            });
        }
        for (int r = 0; r < 4; r++) {
            executor.submit(() -> {
                for (int i = 0; i < 1000; i++) cache.get("key0"); // читатели не должны падать
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();

        assertEquals(writers * perWriter, cache.size());
    }

    // ---------- AsyncLogger: вынос I/O в отдельный поток ----------

    @Test
    @Timeout(5)
    @DisplayName("AsyncLogger: после start/log/stop сообщение записано")
    void testAsyncLoggerBasic() throws InterruptedException {
        AsyncLogger logger = new AsyncLogger();
        logger.start();
        logger.log("hello");
        logger.stop();
        assertTrue(logger.getWritten().contains("hello"));
        assertEquals(1, logger.writtenCount());
    }

    @Test
    @Timeout(10)
    @DisplayName("AsyncLogger: при конкурентной записи и корректном stop ни одно сообщение не теряется")
    void testAsyncLoggerNoMessagesLost() throws InterruptedException {
        AsyncLogger logger = new AsyncLogger();
        logger.start();

        int producers = 8;
        int perProducer = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(producers);
        CountDownLatch latch = new CountDownLatch(producers);

        for (int p = 0; p < producers; p++) {
            int base = p * perProducer;
            executor.submit(() -> {
                for (int i = 0; i < perProducer; i++) logger.log("msg-" + (base + i));
                latch.countDown();
            });
        }

        latch.await();          // все producer'ы завершили вызовы log()
        executor.shutdown();
        logger.stop();          // должен дописать всю очередь

        assertEquals(producers * perProducer, logger.writtenCount(), "ни одно сообщение не должно потеряться");
        Set<String> unique = new HashSet<>(logger.getWritten());
        assertEquals(producers * perProducer, unique.size(), "не должно быть дублей или потерь");
    }
}

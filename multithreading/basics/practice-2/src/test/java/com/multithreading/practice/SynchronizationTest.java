package com.multithreading.practice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Задача: Реализуйте классы и методы так, чтобы все тесты проходили.
 *
 * Требования:
 * 1. Потокобезопасный счетчик с использованием synchronized
 * 2. Потокобезопасный счетчик с использованием AtomicInteger
 * 3. Счетчик с использованием ReentrantLock
 * 4. Кэш с использованием ReadWriteLock
 * 5. Ограниченный буфер (Producer-Consumer) с использованием wait()/notifyAll()
 *
 * Тесты проверяют не только корректный результат под нагрузкой,
 * но и КАКОЙ механизм синхронизации применён (через рефлексию полей).
 * Реализовать всё одним и тем же способом не получится.
 */
class SynchronizationTest {

    private static final int THREAD_COUNT = 10;
    private static final int INCREMENTS_PER_THREAD = 100_000;

    /* Возвращает true, если у класса есть поле, тип которого присваиваем к expected. */
    private static boolean hasFieldAssignableTo(Class<?> target, Class<?> expected) {
        for (Field field : target.getDeclaredFields()) {
            if (expected.isAssignableFrom(field.getType())) {
                return true;
            }
        }
        return false;
    }

    /* Возвращает true, если у класса есть поле из указанного пакета (по префиксу имени типа). */
    private static boolean hasFieldFromPackage(Class<?> target, String packagePrefix) {
        for (Field field : target.getDeclaredFields()) {
            if (field.getType().getName().startsWith(packagePrefix)) {
                return true;
            }
        }
        return false;
    }

    @Test
    @Timeout(10)
    @DisplayName("SynchronizedCounter: корректный счёт под нагрузкой именно через synchronized (без Atomic/Lock)")
    void testSynchronizedCounter() throws InterruptedException {
        // Должен быть реализован на synchronized, а не в обход через Atomic*/Lock
        assertTrue(!hasFieldFromPackage(SynchronizedCounter.class, "java.util.concurrent.atomic.")
                        && !hasFieldAssignableTo(SynchronizedCounter.class, Lock.class)
                        && !hasFieldAssignableTo(SynchronizedCounter.class, ReadWriteLock.class),
                "SynchronizedCounter должен использовать synchronized, а не Atomic*/Lock");

        SynchronizedCounter counter = new SynchronizedCounter();
        runIncrements(counter::increment);

        assertEquals((long) THREAD_COUNT * INCREMENTS_PER_THREAD, counter.getCount(),
                "Счетчик должен быть равен количеству потоков * инкрементов");
    }

    @Test
    @Timeout(10)
    @DisplayName("AtomicCounter: корректный счёт под нагрузкой именно на классе из java.util.concurrent.atomic")
    void testAtomicCounter() throws InterruptedException {
        assertTrue(hasFieldFromPackage(AtomicCounter.class, "java.util.concurrent.atomic."),
                "AtomicCounter должен использовать поле из java.util.concurrent.atomic (например, AtomicInteger)");

        AtomicCounter counter = new AtomicCounter();
        runIncrements(counter::increment);

        assertEquals((long) THREAD_COUNT * INCREMENTS_PER_THREAD, counter.getCount(),
                "Атомарный счетчик должен работать корректно");
    }

    @Test
    @Timeout(10)
    @DisplayName("LockCounter: корректный счёт под нагрузкой именно на java.util.concurrent.locks.Lock")
    void testReentrantLockCounter() throws InterruptedException {
        assertTrue(hasFieldAssignableTo(LockCounter.class, Lock.class),
                "LockCounter должен использовать поле типа Lock (например, ReentrantLock)");

        LockCounter counter = new LockCounter();
        runIncrements(counter::increment);

        assertEquals((long) THREAD_COUNT * INCREMENTS_PER_THREAD, counter.getCount(),
                "Счетчик с ReentrantLock должен работать корректно");
    }

    @Test
    @Timeout(10)
    @DisplayName("ThreadSafeCache: параллельные запись и чтение через ReadWriteLock без потери данных")
    void testReadWriteLockCache() throws InterruptedException {
        assertTrue(hasFieldAssignableTo(ThreadSafeCache.class, ReadWriteLock.class),
                "ThreadSafeCache должен использовать поле типа ReadWriteLock");

        ThreadSafeCache<String, String> cache = new ThreadSafeCache<>();
        int writers = 5;
        int entriesPerWriter = 200;
        ExecutorService executor = Executors.newFixedThreadPool(10);

        // Писатели
        for (int i = 0; i < writers; i++) {
            final int id = i;
            executor.submit(() -> {
                for (int j = 0; j < entriesPerWriter; j++) {
                    cache.put("key" + id + "_" + j, "value" + id + "_" + j);
                }
            });
        }

        // Читатели (работают параллельно с писателями)
        for (int i = 0; i < 5; i++) {
            executor.submit(() -> {
                for (int j = 0; j < entriesPerWriter; j++) {
                    cache.get("key0_" + j);
                }
            });
        }

        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS),
                "Операции с кэшем должны завершиться в отведённое время");

        // Все записи всех писателей должны быть видимы и не повреждены
        for (int id = 0; id < writers; id++) {
            for (int j = 0; j < entriesPerWriter; j++) {
                String key = "key" + id + "_" + j;
                assertEquals("value" + id + "_" + j, cache.get(key),
                        "Кэш должен содержать корректное значение для " + key);
            }
        }
        assertNotNull(cache.get("key0_0"), "Кэш должен содержать записанные значения");
    }

    @Test
    @Timeout(10)
    @DisplayName("BoundedBuffer: один производитель и один потребитель — FIFO без потерь на wait/notify")
    void testBoundedBufferFifoUnderLoad() throws InterruptedException {
        // Должен быть реализован на synchronized + wait()/notifyAll(), без классов из java.util.concurrent
        assertFalse(hasFieldFromPackage(BoundedBuffer.class, "java.util.concurrent."),
                "BoundedBuffer должен использовать synchronized + wait()/notifyAll(), без классов из java.util.concurrent");

        int n = 2000;
        BoundedBuffer<Integer> buffer = new BoundedBuffer<>(16);
        List<Integer> consumed = new ArrayList<>(); // пишет только consumer-поток; читаем после join

        Thread producer = new Thread(() -> {
            try {
                for (int i = 0; i < n; i++) {
                    buffer.put(i);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        Thread consumer = new Thread(() -> {
            try {
                for (int i = 0; i < n; i++) {
                    consumed.add(buffer.take());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        consumer.start(); // стартуем раньше: take() должен заблокироваться на пустом буфере
        producer.start();
        producer.join(5000);
        consumer.join(5000);

        assertEquals(n, consumed.size(), "Должны быть получены все элементы без потерь");
        for (int i = 0; i < n; i++) {
            assertEquals(i, consumed.get(i), "Порядок элементов должен быть FIFO");
        }
    }

    @Test
    @Timeout(10)
    @DisplayName("BoundedBuffer: put блокируется на заполненном буфере, size не превышает ёмкость")
    void testPutBlocksWhenFull() throws InterruptedException {
        BoundedBuffer<String> buffer = new BoundedBuffer<>(1);
        buffer.put("a");
        assertEquals(1, buffer.size());

        Thread producer = new Thread(() -> {
            try {
                buffer.put("b"); // буфер полон — должен заблокироваться
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        producer.start();
        Thread.sleep(300); // даём потоку дойти до wait()

        assertTrue(producer.getState() == Thread.State.WAITING
                        || producer.getState() == Thread.State.TIMED_WAITING,
                "put() должен заблокироваться, пока буфер заполнен");
        assertEquals(1, buffer.size(), "Размер не должен превышать ёмкость");

        assertEquals("a", buffer.take()); // освобождаем место
        producer.join(2000);
        assertFalse(producer.isAlive(), "После take() заблокированный put() должен завершиться");
        assertEquals(1, buffer.size());
    }

    @Test
    @Timeout(10)
    @DisplayName("BoundedBuffer: take блокируется на пустом буфере и пробуждается после put")
    void testTakeBlocksWhenEmpty() throws InterruptedException {
        BoundedBuffer<String> buffer = new BoundedBuffer<>(4);
        AtomicReference<String> taken = new AtomicReference<>();

        Thread consumer = new Thread(() -> {
            try {
                taken.set(buffer.take()); // буфер пуст — должен заблокироваться
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        consumer.start();
        Thread.sleep(300);

        assertTrue(consumer.getState() == Thread.State.WAITING
                        || consumer.getState() == Thread.State.TIMED_WAITING,
                "take() должен заблокироваться, пока буфер пуст");
        assertNull(taken.get(), "До put() потребитель не должен получить элемент");

        buffer.put("x");
        consumer.join(2000);
        assertFalse(consumer.isAlive(), "После put() заблокированный take() должен завершиться");
        assertEquals("x", taken.get());
    }

    @Test
    @Timeout(15)
    @DisplayName("TransferAccount: встречные переводы через tryLock без дедлока, сумма балансов сохраняется")
    void testTransferNoDeadlock() throws InterruptedException {
        // Должен быть реализован на ReentrantLock (tryLock), а не на synchronized/Atomic
        assertTrue(hasFieldAssignableTo(TransferAccount.class, Lock.class)
                        && !hasFieldFromPackage(TransferAccount.class, "java.util.concurrent.atomic."),
                "TransferAccount должен использовать ReentrantLock и tryLock()");

        TransferAccount a = new TransferAccount(1_000_000);
        TransferAccount b = new TransferAccount(1_000_000);
        long total = a.getBalance() + b.getBalance();

        int threads = 8;
        int transfersPerThread = 50_000;
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        // Половина потоков переводит A->B, половина B->A — встречные захваты двух замков.
        for (int i = 0; i < threads; i++) {
            final boolean aToB = (i % 2 == 0);
            executor.submit(() -> {
                for (int j = 0; j < transfersPerThread; j++) {
                    if (aToB) {
                        a.transferTo(b, 1);
                    } else {
                        b.transferTo(a, 1);
                    }
                }
            });
        }

        executor.shutdown();
        assertTrue(executor.awaitTermination(12, TimeUnit.SECONDS),
                "Переводы должны завершиться без дедлока");

        assertEquals(total, a.getBalance() + b.getBalance(),
                "Сумма балансов должна сохраняться при любой конкуренции (перевод атомарен)");
    }

    /* Запускает THREAD_COUNT потоков, каждый из которых вызывает action INCREMENTS_PER_THREAD раз. */
    private void runIncrements(Runnable action) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                for (int j = 0; j < INCREMENTS_PER_THREAD; j++) {
                    action.run();
                }
            });
        }
        executor.shutdown();
        assertTrue(executor.awaitTermination(5, TimeUnit.SECONDS),
                "Все потоки должны завершиться в отведённое время");
    }
}

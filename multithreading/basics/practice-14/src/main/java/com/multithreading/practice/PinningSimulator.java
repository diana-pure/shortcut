package com.multithreading.practice;

/**
 * Демонстрация pinning виртуальных потоков (Java 21–23).
 *
 * Pinning — ситуация, когда виртуальный поток НЕ может демонтироваться (unmount)
 * от потока-носителя (carrier thread) во время блокировки и держит носитель занятым.
 * Если носителей не хватает, масштабируемость теряется.
 *
 * На Java 21–23 главная причина pinning — блокирующий вызов внутри synchronized-блока.
 * Лечится заменой synchronized на ReentrantLock: его блокировка реализована через
 * LockSupport.park(), при котором JVM корректно демонтирует виртуальный поток.
 *
 * ВНИМАНИЕ: в Java 24 (JEP 491) pinning на synchronized устранён — на 24+ оба метода
 * ниже работают одинаково (тест это учитывает и пропускается на 24+).
 *
 * Реализуйте оба метода. Каждая задача должна захватывать СВОЙ объект-монитор
 * (моделируем ситуацию, когда множество запросов одновременно держат свои замки)
 * и внутри критической секции блокироваться через Thread.sleep(sleepMs) —
 * это имитация блокирующего I/O (socket.read(), JDBC и т.п.).
 *
 *   runWithSynchronized(tasksCount, sleepMs):
 *     - tasksCount виртуальных потоков через newVirtualThreadPerTaskExecutor()
 *     - в каждом: synchronized (собственный объект-монитор) { Thread.sleep(sleepMs); }
 *     - дождаться завершения всех задач
 *     → на Java 21–23 это вызывает pinning
 *
 *   runWithReentrantLock(tasksCount, sleepMs):
 *     - то же самое, но критическая секция защищена собственным ReentrantLock:
 *       lock.lock(); try { Thread.sleep(sleepMs); } finally { lock.unlock(); }
 *     → pinning НЕ возникает ни на одной версии
 *
 * Подсказка: try-with-resources для ExecutorService дождётся всех задач на close().
 */
public class PinningSimulator {

    public void runWithSynchronized(int tasksCount, int sleepMs) throws InterruptedException {
    }

    public void runWithReentrantLock(int tasksCount, int sleepMs) throws InterruptedException {
    }
}

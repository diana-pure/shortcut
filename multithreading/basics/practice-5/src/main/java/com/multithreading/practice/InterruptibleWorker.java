package com.multithreading.practice;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ЗАДАЧА НА ОБРАБОТКУ InterruptedException.
 *
 * Воркер в цикле забирает элементы из BlockingQueue через take() и считает их.
 * Когда поток прерывают (Thread.interrupt() или ExecutorService.shutdownNow()),
 * take() бросает InterruptedException. Правильная реакция:
 *   1) НЕ глотать исключение молча;
 *   2) восстановить флаг прерывания — Thread.currentThread().interrupt();
 *   3) корректно выйти из цикла (завершить работу).
 *
 * --- КАК НЕ НАДО ---
 *   while (true) {
 *       try { queue.take(); }
 *       catch (InterruptedException e) { ignore }  // флаг потерян, цикл не выйдет
 *   }
 *
 * --- КАК НАДО ---
 *   try {
 *       while (true) { queue.take(); processed.incrementAndGet(); }
 *   } catch (InterruptedException e) {
 *       Thread.currentThread().interrupt(); // восстановили флаг
 *       // выходим из метода — работа завершена
 *   }
 *
 * Тест проверяет: воркер завершился после interrupt(), обработал часть элементов
 * и оставил флаг прерывания восстановленным (interruptFlagRestored == true).
 */
public class InterruptibleWorker {

    private final BlockingQueue<Integer> queue;
    private final AtomicInteger processed = new AtomicInteger(0);
    private volatile boolean interruptFlagRestored = false;

    public InterruptibleWorker(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    /**
     * Крутится на queue.take(), считая обработанные элементы, пока поток не прервут.
     * При прерывании: восстановить флаг, выставить interruptFlagRestored и выйти.
     */
    public void runUntilInterrupted() {
    }

    public int getProcessedCount() {
        return processed.get();
    }

    /**
     * true, если после прерывания флаг прерывания был корректно восстановлен.
     */
    public boolean wasInterruptFlagRestored() {
        return interruptFlagRestored;
    }
}

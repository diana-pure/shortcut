package com.multithreading.practice;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Процессор, выполняющий задачи в несколько синхронизированных фаз.
 *
 * Реализуйте методы:
 * - Конструктор принимает int threadCount и int phaseCount
 * - runPhase(threadIndex, phaseAction) — выполняет phaseAction и ждёт на барьере
 *   Сигнатура: void runPhase(int threadIndex, Runnable phaseAction)
 * - getCompletedPhases() — количество завершённых фаз (AtomicInteger, инкрементируется в barrierAction CyclicBarrier)
 *
 * Используйте CyclicBarrier(threadCount, barrierAction) где barrierAction инкрементирует completedPhases.
 */
public class PhaseProcessor {

    public PhaseProcessor(int threadCount, int phaseCount) {
    }

    public void runPhase(int threadIndex, Runnable phaseAction) throws BrokenBarrierException, InterruptedException {
    }

    public int getCompletedPhases() {
        return 0;
    }
}

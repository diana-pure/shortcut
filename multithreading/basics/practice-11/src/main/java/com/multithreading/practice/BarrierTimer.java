package com.multithreading.practice;

/**
 * Барьерное действие для CyclicBarrier, измеряющее время параллельной фазы теста.
 *
 * Передаётся вторым аргументом в new CyclicBarrier(parties, barrierAction) и выполняется
 * последним прибывшим потоком в момент срабатывания барьера. Первый раз барьер срабатывает
 * на «стартовом выстреле» (фиксируем startTime), второй — когда все потоки закончили работу
 * (фиксируем endTime). getTime() возвращает реальное wall-clock время параллельной работы.
 *
 * Реализуйте:
 * - run()    — synchronized: при первом вызове запомнить startTime = System.nanoTime() и
 *              выставить started = true; при последующем — endTime = System.nanoTime()
 * - clear()  — synchronized: сбросить started = false (для переиспользования)
 * - getTime()— synchronized: вернуть endTime - startTime
 */
public class BarrierTimer implements Runnable {

    @Override
    public synchronized void run() {
    }

    public synchronized void clear() {
    }

    public synchronized long getTime() {
        return 0L;
    }
}

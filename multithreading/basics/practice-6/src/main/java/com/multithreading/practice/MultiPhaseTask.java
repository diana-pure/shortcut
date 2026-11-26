package com.multithreading.practice;

import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Многоступенчатая задача на основе Phaser с динамическим числом участников.
 *
 * В отличие от CyclicBarrier, число участников можно менять во время работы:
 * добавлять через register() и убирать через arriveAndDeregister().
 *
 * Реализуйте методы:
 * - Конструктор принимает int parties — начальное число участников
 * - arriveAndAwait() — прийти на фазу и ждать остальных участников (аналог CyclicBarrier.await())
 * - register() — динамически добавить нового участника
 * - arriveAndDeregister() — прийти и навсегда покинуть фазер, не дожидаясь остальных
 * - getCompletedPhases() — число завершённых фаз
 * - getRegisteredParties() — текущее число зарегистрированных участников
 *
 * Используйте Phaser с переопределённым onAdvance(int phase, int registeredParties):
 *  - инкрементируйте completedPhases на каждом продвижении фазы;
 *  - возвращайте (registeredParties == 0), чтобы фазер автоматически терминировался,
 *    когда не осталось участников.
 */
public class MultiPhaseTask {

    public MultiPhaseTask(int parties) {
    }

    public void arriveAndAwait() {
    }

    public void register() {
    }

    public void arriveAndDeregister() {
    }

    public int getCompletedPhases() {
        return 0;
    }

    public int getRegisteredParties() {
        return 0;
    }
}

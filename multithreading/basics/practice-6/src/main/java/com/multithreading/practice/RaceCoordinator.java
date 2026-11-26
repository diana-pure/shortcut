package com.multithreading.practice;

import java.util.concurrent.CountDownLatch;

/**
 * Координатор старта и финиша гонки на основе CountDownLatch.
 *
 * Здесь сочетаются сразу три паттерна защёлки:
 *  - барьер готовности: организатор ждёт, пока все участники будут готовы;
 *  - «стартовый пистолет»: один сигнал старта открывает ворота сразу всем;
 *  - «финишная черта»: организатор ждёт, пока все финишируют.
 *
 * Реализуйте методы:
 * - Конструктор принимает int participantCount
 * - readyUp() — участник сигнализирует о готовности (отсчёт readyLatch)
 * - awaitStart() — участник ждёт «выстрела» стартового пистолета
 * - startRace() — организатор дожидается готовности всех участников, затем даёт старт
 * - finish() — участник финишировал (отсчёт finishLatch)
 * - awaitAllFinish() — ждёт, пока все финишируют
 *
 * Используйте три CountDownLatch:
 *  1. readyLatch(participantCount) — барьер готовности участников
 *  2. startLatch(1)                — стартовый пистолет (один countDown открывает всех)
 *  3. finishLatch(participantCount) — ожидание финиша всех участников
 */
public class RaceCoordinator {

    public RaceCoordinator(int participantCount) {
    }

    public void readyUp() {
    }

    public void awaitStart() throws InterruptedException {
    }

    public void startRace() {
    }

    public void finish() {
    }

    public void awaitAllFinish() throws InterruptedException {
    }
}

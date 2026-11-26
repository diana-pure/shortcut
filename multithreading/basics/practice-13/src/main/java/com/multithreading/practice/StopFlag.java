package com.multithreading.practice;

/**
 * Флаг остановки потока на основе volatile.
 *
 * ПРОБЛЕМА: без volatile JIT-компилятор может закешировать значение поля
 * в регистр процессора и поток-читатель никогда не увидит изменение.
 *
 * Реализуйте:
 * - stopped — поле типа boolean (добавьте volatile!)
 * - requestStop() — устанавливает stopped = true
 * - isStopped() — возвращает stopped
 *
 * Правило: поле должно быть объявлено как `volatile boolean stopped`.
 * Без volatile тест testVolatileStopsThread может зависнуть навсегда.
 */
public class StopFlag {

    private boolean stopped;

    public void requestStop() {
    }

    public boolean isStopped() {
        return false;
    }
}

package com.multithreading.practice;

import java.util.concurrent.CountDownLatch;

/**
 * Задание 6. Защёлка с результатом ValueLatch (§6 главы 8).
 *
 * В ConcurrentPuzzleSolver результат, найденный любым из рабочих потоков, публикуется
 * через «защёлку с результатом» на базе CountDownLatch. Она гарантирует, что значение
 * установится ровно один раз (первый писатель побеждает), и блокирует читателя до его
 * появления.
 *
 * Поля done (CountDownLatch со счётчиком 1) и value уже объявлены. Реализуйте:
 *
 * 1. isSet(): true, если значение уже установлено (done.getCount() == 0).
 *
 * 2. setValue(T newValue) — synchronized:
 *    - если !isSet(): сохранить newValue в value и вызвать done.countDown();
 *    - если значение уже установлено — ничего не делать (первый вызов побеждает).
 *
 * 3. getValue() throws InterruptedException:
 *    - дождаться установки: done.await();
 *    - вернуть value (чтение внутри synchronized(this) для безопасной публикации).
 */
public class ValueLatch<T> {

    private final CountDownLatch done = new CountDownLatch(1);
    private T value = null;

    public boolean isSet() {
        return false;
    }

    public synchronized void setValue(T newValue) {
    }

    public T getValue() throws InterruptedException {
        return null;
    }
}

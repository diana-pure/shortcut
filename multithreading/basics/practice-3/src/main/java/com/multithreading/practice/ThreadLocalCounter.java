package com.multithreading.practice;

/**
 * Счётчик, изолированный по потокам через ThreadLocal.
 * Каждый поток должен иметь независимое значение счётчика.
 *
 * Задача:
 * 1. Объявите поле ThreadLocal<Integer> с начальным значением 0.
 * 2. Реализуйте increment() и get().
 * 3. Реализуйте reset() через remove(): в пуле потоки переиспользуются,
 *    и без очистки значение «протечёт» в следующую задачу того же потока.
 */
public class ThreadLocalCounter {

    public void increment() {
    }

    public int get() {
        return 0;
    }

    public void reset() {
    }
}

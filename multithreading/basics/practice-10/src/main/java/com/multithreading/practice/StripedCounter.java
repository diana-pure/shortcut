package com.multithreading.practice;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Счётчик с полосным разделением блокировок (lock striping).
 *
 * Вместо одного замка на весь объект — массив из stripeCount замков.
 * Ключ распределяется по полосам: stripe = Math.abs(key.hashCode()) % stripeCount.
 *
 * Реализуйте:
 * - Конструктор принимает int stripeCount (количество полос)
 * - increment(String key) — атомарно увеличивает счётчик для key
 *   (блокирует только соответствующую полосу)
 * - get(String key) — возвращает значение счётчика для key
 * - getStripeCount() — количество полос
 *
 * Используйте: ReentrantLock[] locks и ConcurrentHashMap<String, Long> для хранения счётчиков.
 */
public class StripedCounter {

    public StripedCounter(int stripeCount) {
    }

    public void increment(String key) {
    }

    public long get(String key) {
        return 0L;
    }

    public int getStripeCount() {
        return 0;
    }
}

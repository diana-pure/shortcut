package com.multithreading.practice;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Лениво инициализируемый Singleton с безопасной публикацией.
 *
 * В текущей реализации getInstance() небезопасен в многопоточной среде:
 * проверка-и-создание (check-then-act) не атомарна, поэтому несколько потоков
 * могут создать несколько экземпляров. К тому же без volatile другой поток
 * мог бы увидеть частично сконструированный объект (см. Double-Checked Locking).
 *
 * Задача:
 * 1. Пометьте поле instance как volatile.
 * 2. Реализуйте getInstance() через double-checked locking:
 *    - первая проверка instance == null без блокировки;
 *    - synchronized (SafeSingleton.class) { ... };
 *    - повторная проверка внутри блока перед созданием.
 *
 * Счётчик CREATION_COUNT помогает проверить, что экземпляр создаётся ровно один раз.
 */
public class SafeSingleton {

    private static SafeSingleton instance;

    private static final AtomicInteger CREATION_COUNT = new AtomicInteger(0);

    private SafeSingleton() {
        CREATION_COUNT.incrementAndGet();
    }

    public static SafeSingleton getInstance() {
        if (instance == null) {
            instance = new SafeSingleton();
        }
        return instance;
    }

    public static int getCreationCount() {
        return CREATION_COUNT.get();
    }
}

package com.multithreading.practice;

/**
 * Задание 1 — устранение состояния гонки (race condition).
 *
 * Потокобезопасный счётчик. Реализуйте через AtomicInteger или synchronized:
 *
 * 1. increment():
 *    - атомарно увеличивает значение счётчика на 1;
 *    - вариант А: AtomicInteger.incrementAndGet();
 *    - вариант Б: synchronized-метод над обычным int.
 *
 * 2. get():
 *    - возвращает текущее значение, согласованное с increment() (видит последнюю запись).
 *
 * Тест: 10 потоков × 1000 инкрементов должны дать ровно 10 000.
 * Без синхронизации часть операций ++ потеряется (read-modify-write не атомарен).
 * Помните: volatile обеспечивает видимость, но НЕ атомарность инкремента.
 */
public class SafeCounter {

    public void increment() {
    }

    public int get() {
        return 0;
    }
}

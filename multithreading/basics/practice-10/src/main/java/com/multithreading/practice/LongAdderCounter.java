package com.multithreading.practice;

import java.util.concurrent.atomic.LongAdder;

/**
 * Счётчик на LongAdder — решение проблемы «горячего поля» (hot field).
 *
 * AtomicLong при высокой конкуренции страдает от CAS-промахов: все потоки бьются
 * за одну ячейку памяти. LongAdder распределяет запись по нескольким ячейкам
 * (Striped64): каждый поток инкрементирует свою, а sum() складывает их при чтении.
 * Под нагрузкой это масштабируется заметно лучше AtomicLong.
 *
 * Реализуйте increment/add/sum/reset, делегируя их экземпляру LongAdder.
 *
 * Для самостоятельного эксперимента: замените LongAdder на AtomicLong и сравните
 * время выполнения теста testLongAdderConcurrency под высокой нагрузкой.
 */
public class LongAdderCounter {

    private final LongAdder adder = new LongAdder();

    public void increment() {
    }

    public void add(long value) {
    }

    public long sum() {
        return 0L;
    }

    public void reset() {
    }
}

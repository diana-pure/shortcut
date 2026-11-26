package com.multithreading.practice;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Атомарное обновление ДВУХ связанных полей с сохранением инварианта lower &lt;= upper.
 *
 * ПРОБЛЕМА: два отдельных volatile-поля (lower, upper) обновить атомарно нельзя —
 * между записью первого и второго другой поток увидит несогласованную пару
 * и инвариант lower &lt;= upper может временно нарушиться.
 *
 * РЕШЕНИЕ: завернуть оба значения в иммутабельный объект IntPair и хранить
 * AtomicReference&lt;IntPair&gt;. Обновление — через CAS-цикл:
 *   снимок -&gt; проверка инварианта -&gt; новый IntPair -&gt; compareAndSet -&gt; повтор при промахе.
 *
 * Реализуйте:
 * - getLower() / getUpper() — чтение через values.get()
 * - setLower(i): если i &gt; текущего upper — бросить IllegalArgumentException("lower > upper");
 *                иначе атомарно записать новый IntPair(i, upper)
 * - setUpper(i): симметрично; если i &lt; текущего lower — IllegalArgumentException("upper < lower")
 *
 * Вложенный класс IntPair уже предоставлен — он иммутабелен (оба поля final), не изменяйте его.
 */
public class NumberRange {

    private static final class IntPair {
        final int lower;
        final int upper;

        IntPair(int lower, int upper) {
            this.lower = lower;
            this.upper = upper;
        }
    }

    private final AtomicReference<IntPair> values =
            new AtomicReference<>(new IntPair(0, 0));

    public int getLower() {
        return 0;
    }

    public int getUpper() {
        return 0;
    }

    public void setLower(int i) {
    }

    public void setUpper(int i) {
    }
}

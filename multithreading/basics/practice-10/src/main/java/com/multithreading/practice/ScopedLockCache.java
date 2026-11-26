package com.multithreading.practice;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Кэш, иллюстрирующий приём «сужение области блокировки» («get in, get out»).
 *
 * Блокировку нужно удерживать ТОЛЬКО на время доступа к разделяемому состоянию (Map),
 * а дорогую операцию вычисления значения выполнять ВНЕ блокировки — иначе один поток,
 * занятый долгим вычислением, заблокирует всех остальных.
 *
 * Реализуйте:
 * - getOrCompute(String key, Function<String,String> expensiveCompute):
 *     1) под synchronized(this) прочитайте значение из Map; если оно есть — верните его;
 *     2) ВНЕ блокировки вызовите expensiveCompute.apply(key);
 *     3) снова под synchronized(this) положите результат в Map и верните его
 *        (учтите, что другой поток мог успеть вычислить тот же ключ).
 * - size() — текущее число записей (под блокировкой).
 */
public class ScopedLockCache {

    private final Map<String, String> cache = new HashMap<>();

    public String getOrCompute(String key, Function<String, String> expensiveCompute) {
        return null;
    }

    public int size() {
        return 0;
    }
}

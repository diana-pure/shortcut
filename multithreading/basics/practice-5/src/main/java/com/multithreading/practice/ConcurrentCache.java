package com.multithreading.practice;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Потокобезопасный кэш на основе ConcurrentHashMap.
 *
 * Реализуйте методы так, чтобы все тесты проходили.
 *
 * Подсказки:
 * - put(key, value) — добавляет или обновляет значение
 * - get(key) — возвращает значение или null
 * - getOrCompute(key, loader) — атомарно вычисляет значение если ключа нет (используйте computeIfAbsent)
 * - size() — текущий размер кэша
 * - computeValue(key, remapping) — атомарно обновляет значение через compute()
 * - mergeValue(key, value, merger) — объединяет через merge()
 * - incrementCounter(key) — атомарно инкрементирует счётчик для ключа
 * - getCounter(key) — возвращает AtomicInteger счётчик
 */
public class ConcurrentCache {

    public void put(String key, Object value) {
    }

    public Object get(String key) {
        return null;
    }

    public Object getOrCompute(String key, Function<String, Object> loader) {
        return null;
    }

    public int size() {
        return 0;
    }

    public void computeValue(String key, BiFunction<String, Integer, Integer> remapping) {
    }

    public void mergeValue(String key, Integer value, BiFunction<Integer, Integer, Integer> merger) {
    }

    public void incrementCounter(String key) {
    }

    public AtomicInteger getCounter(String key) {
        return null;
    }
}

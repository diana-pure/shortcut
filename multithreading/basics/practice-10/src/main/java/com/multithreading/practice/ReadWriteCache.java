package com.multithreading.practice;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Кэш на ReentrantReadWriteLock — альтернатива монопольной блокировке (§4.5).
 *
 * Выгоден при сценарии «много чтений, редкие записи» (кэш конфигурации):
 * читатели не блокируют друг друга и работают параллельно под readLock,
 * писатель получает монопольный доступ под writeLock.
 *
 * Реализуйте, используя поле lock:
 * - get(key)      — чтение значения под readLock;
 * - put(key,val)  — запись под writeLock;
 * - size()        — размер под readLock;
 * - readSnapshot(reader) — выполнить reader.apply(map) под readLock
 *                  (несколько читателей должны выполнять его параллельно).
 *
 * Важно: readLock берут читатели (get/size/readSnapshot), writeLock — писатели (put).
 * Не оборачивайте чтение в writeLock — иначе потеряете параллелизм читателей.
 * Не забывайте unlock в finally.
 */
public class ReadWriteCache {

    private final Map<String, String> map = new HashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public String get(String key) {
        return null;
    }

    public void put(String key, String value) {
    }

    public int size() {
        return 0;
    }

    public <T> T readSnapshot(Function<Map<String, String>, T> reader) {
        return null;
    }
}

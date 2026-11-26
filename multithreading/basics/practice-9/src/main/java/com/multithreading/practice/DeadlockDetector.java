package com.multithreading.practice;

import java.util.Set;

/**
 * Задание 6 — программное обнаружение взаимоблокировки через JMX (§3.2).
 *
 * Предотвратить дедлок — наша задача, но JVM умеет его ОБНАРУЖИВАТЬ уже после факта.
 * Помимо дампа потока (kill -3 / Ctrl-\), взаимоблокировку можно найти прямо из
 * приложения через {@link java.lang.management.ThreadMXBean} — это удобно встроить
 * в healthcheck или мониторинг, чтобы поднять алерт вместо «тихого» зависания.
 *
 * ЗАДАЧА: реализуйте findDeadlockedThreadNames() так, чтобы метод возвращал
 * имена всех потоков, находящихся во взаимоблокировке, и пустой набор, если
 * взаимоблокировок нет.
 *
 * Подсказка — ThreadMXBean:
 *   ThreadMXBean bean = ManagementFactory.getThreadMXBean();
 *   long[] ids = bean.findDeadlockedThreads();   // null, если дедлоков нет!
 *   - findDeadlockedThreads() ловит циклы и на мониторах (synchronized),
 *     и на ownable synchronizer (ReentrantLock);
 *   - по id получите ThreadInfo через bean.getThreadInfo(ids) и соберите имена
 *     (ThreadInfo.getThreadName()); элемент массива может быть null — пропускайте;
 *   - ВАЖНО: при отсутствии взаимоблокировок findDeadlockedThreads() возвращает
 *     именно null, а не пустой массив — обработайте это и верните пустой набор.
 */
public class DeadlockDetector {

    public static Set<String> findDeadlockedThreadNames() {
        return Set.of();
    }
}

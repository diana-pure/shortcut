package com.multithreading.practice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Задание 7. Корректное завершение пула (§5 главы 8).
 *
 * Пул потоков нужно явно останавливать, иначе его не-daemon потоки не дадут JVM
 * завершиться. Канонический паттерн graceful shutdown:
 *   shutdown() → awaitTermination(timeout) → при таймауте shutdownNow().
 *
 * Реализуйте shutdownGracefully(pool, timeout, unit):
 * 1. pool.shutdown() — перестаём принимать новые задачи, уже принятые доигрывают;
 * 2. если pool.awaitTermination(timeout, unit) вернул true — пул успел завершиться,
 *    верните true (мягкое завершение);
 * 3. иначе вызовите pool.shutdownNow() (прерывание выполняющихся задач), при желании
 *    ещё раз дождитесь завершения через awaitTermination и верните false.
 *
 * Метод объявлен throws InterruptedException (его бросает awaitTermination).
 */
public class PoolShutdown {

    private PoolShutdown() {
    }

    public static boolean shutdownGracefully(ExecutorService pool, long timeout, TimeUnit unit)
            throws InterruptedException {
        return false;
    }
}

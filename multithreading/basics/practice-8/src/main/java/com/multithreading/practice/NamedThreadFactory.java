package com.multithreading.practice;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Задание 3. Настраиваемая ThreadFactory.
 *
 * Когда пулу нужно создать поток, он делает это через ThreadFactory. По умолчанию
 * потоки называются pool-1-thread-1 и т.д. — нечитаемо в thread dump. Настраиваемая
 * фабрика задаёт осмысленные имена, daemon-статус и UncaughtExceptionHandler.
 *
 * Реализуйте newThread(Runnable r) так, чтобы он:
 * 1. создавал поток на основе r;
 * 2. задавал имя в формате "<prefix>-<N>", где N — счётчик, начинающийся с 1
 *    (первый поток "<prefix>-1", второй "<prefix>-2", ...). Используйте поле counter;
 * 3. устанавливал daemon-статус через setDaemon(daemon);
 * 4. устанавливал обработчик необработанных исключений:
 *    setUncaughtExceptionHandler(handler);
 * 5. возвращал настроенный поток.
 */
public class NamedThreadFactory implements ThreadFactory {

    private final AtomicInteger counter = new AtomicInteger(0);
    private final String prefix;
    private final boolean daemon;
    private final Thread.UncaughtExceptionHandler handler;

    public NamedThreadFactory(String prefix, boolean daemon, Thread.UncaughtExceptionHandler handler) {
        this.prefix = prefix;
        this.daemon = daemon;
        this.handler = handler;
    }

    @Override
    public Thread newThread(Runnable r) {
        return null;
    }
}

package com.multithreading.practice;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * Обработчик задач на виртуальных потоках (Java 21+).
 *
 * Реализуйте методы:
 *
 * 1. executeWithVirtualThreads(List<Runnable> tasks) → int
 *    - Создайте ExecutorService через Executors.newVirtualThreadPerTaskExecutor()
 *    - Отправьте все задачи (submit)
 *    - Дождитесь завершения (shutdown + awaitTermination или try-with-resources)
 *    - Верните количество успешно выполненных задач
 *
 * 2. executeAndCollect(List<Callable<T>> tasks) → List<T>
 *    - Используйте Executors.newVirtualThreadPerTaskExecutor()
 *    - Отправьте все Callable, соберите Future<T>
 *    - Получите результаты (future.get()), соберите в список
 *    - Верните список результатов (порядок должен совпадать с порядком tasks)
 *
 * 3. isVirtualThread(Thread t) → boolean
 *    - Возвращает t.isVirtual() (метод доступен с Java 21)
 *
 * Подсказка: используйте try-with-resources для ExecutorService
 * try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) { ... }
 */
public class VirtualThreadProcessor {

    public int executeWithVirtualThreads(List<Runnable> tasks) throws InterruptedException {
        return 0;
    }

    public <T> List<T> executeAndCollect(List<Callable<T>> tasks) throws InterruptedException, java.util.concurrent.ExecutionException {
        return List.of();
    }

    public boolean isVirtualThread(Thread t) {
        return false;
    }
}

package com.multithreading.practice;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Демонстрация операций CompletableFuture.
 *
 * Реализуйте каждый метод согласно описанию ниже.
 */
public class AsyncTaskProcessor {

    /**
     * Цепочка преобразований строки.
     *
     * Шаги:
     * 1. CompletableFuture.supplyAsync(() -> input)
     * 2. thenApply(s -> s + " WORLD")   — добавить " WORLD" к строке
     * 3. thenApply(String::toUpperCase) — привести к верхнему регистру
     *
     * Пример: "Hello" → "Hello WORLD" → "HELLO WORLD"
     */
    public CompletableFuture<String> createTransformationChain(String input) {
        return null;
    }

    /**
     * Плоское отображение (flatMap) через thenCompose.
     *
     * Шаги:
     * 1. CompletableFuture.supplyAsync(() -> value)
     * 2. thenCompose(v -> CompletableFuture.supplyAsync(() -> "Result: " + v))
     *
     * Пример: 10 → "Result: 10"
     */
    public CompletableFuture<String> createComposition(int value) {
        return null;
    }

    /**
     * Объединение двух независимых Future.
     *
     * Шаги:
     * 1. CompletableFuture.supplyAsync(() -> s1)
     * 2. CompletableFuture.supplyAsync(() -> s2)
     * 3. future1.thenCombine(future2, (a, b) -> a + " " + b)
     *
     * Пример: "Hello", "World" → "Hello World"
     */
    public CompletableFuture<String> combineResults(String s1, String s2) {
        return null;
    }

    /**
     * Ожидание завершения всех переданных Future.
     *
     * Используйте CompletableFuture.allOf(futures).
     */
    public CompletableFuture<Void> waitForAll(CompletableFuture<?>... futures) {
        return null;
    }

    /**
     * Обработка ошибки через exceptionally.
     *
     * Если shouldFail == true:
     *   - supplyAsync бросает new RuntimeException("ошибка")
     *   - exceptionally возвращает "Обработано: " + ex.getMessage()
     * Если shouldFail == false:
     *   - supplyAsync возвращает "OK"
     *   - exceptionally не вызывается
     */
    public CompletableFuture<String> handleError(boolean shouldFail) {
        return null;
    }

    /**
     * Двусторонняя обработка через handle (работает и при успехе, и при ошибке).
     *
     * Если shouldFail == false:
     *   - supplyAsync возвращает "успех"
     *   - handle: result.toUpperCase() → "УСПЕХ"
     * Если shouldFail == true:
     *   - supplyAsync бросает RuntimeException
     *   - handle: возвращает "Обработана ошибка"
     *
     * Сигнатура handle: (result, ex) -> { if (ex != null) ... else ... }
     */
    public CompletableFuture<String> handleSuccessOrError(boolean shouldFail) {
        return null;
    }

    /**
     * Потребление результата через thenAccept (терминальная операция, не возвращает значение).
     *
     * Шаги:
     * 1. CompletableFuture.supplyAsync(() -> value)
     * 2. thenAccept(consumer)
     *
     * Верните результат thenAccept (CompletableFuture<Void>).
     */
    public CompletableFuture<Void> processResult(int value, Consumer<Integer> consumer) {
        return null;
    }
}

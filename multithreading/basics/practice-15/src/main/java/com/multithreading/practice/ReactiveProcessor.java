package com.multithreading.practice;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

/**
 * Реактивный процессор на базе Project Reactor.
 *
 * Реализуйте все методы. Каждый метод должен возвращать ленивый поток (без subscribe).
 *
 * Подсказки:
 * - Mono.just(value) — Mono с одним значением
 * - Flux.just(values...) — Flux с несколькими значениями
 * - flux.map(fn) — трансформация каждого элемента
 * - flux.filter(predicate) — фильтрация
 * - flux.flatMap(fn) — разворачивает каждый элемент в поток (порядок НЕ гарантирован)
 * - flux.concatMap(fn) — разворачивает каждый элемент в поток с сохранением порядка
 * - Flux.merge(f1, f2) — объединяет потоки параллельно (не гарантирует порядок)
 * - Flux.concat(f1, f2) — объединяет потоки последовательно (порядок сохраняется)
 * - Flux.zip(f1, f2, combiner) — комбинирует попарно
 * - flux.onErrorReturn(value) — при ошибке — вернуть значение
 * - flux.onErrorResume(fn) — при ошибке — переключиться на другой поток
 * - flux.retry(n) — повторно подписаться при ошибке (n попыток)
 * - Mono.fromSupplier(supplier).subscribeOn(Schedulers.boundedElastic()) — блокирующий вызов на пуле boundedElastic
 */
public class ReactiveProcessor {

    /** Создаёт Mono с переданным значением */
    public <T> Mono<T> createMono(T value) {
        return Mono.empty();
    }

    /** Создаёт Flux с переданными значениями */
    @SafeVarargs
    public final <T> Flux<T> createFlux(T... values) {
        return Flux.empty();
    }

    /** Преобразует все строки в верхний регистр */
    public Flux<String> transformToUpperCase(Flux<String> input) {
        return input;
    }

    /** Оставляет только чётные числа */
    public Flux<Integer> filterEvenNumbers(Flux<Integer> input) {
        return input;
    }

    /**
     * Разворачивает каждый элемент n в поток из n чисел (1, 2, ..., n).
     * Например: [1, 2, 3] → [1, 1, 2, 1, 2, 3] (итого 1+2+3 = 6 элементов)
     */
    public Flux<Integer> expandWithFlatMap(Flux<Integer> input) {
        return input;
    }

    /** Объединяет два потока (порядок элементов не гарантирован) */
    public <T> Flux<T> mergeFluxes(Flux<T> flux1, Flux<T> flux2) {
        return Flux.empty();
    }

    /**
     * Комбинирует потоки имён и возрастов попарно: "Alice is 25"
     */
    public Flux<String> zipFluxes(Flux<String> names, Flux<Integer> ages) {
        return Flux.empty();
    }

    /** При ошибке — добавляет fallback значение в конец потока */
    public Flux<String> handleErrorWithReturn(Flux<String> input, String fallback) {
        return input;
    }

    /** При ошибке — переключается на recovery поток */
    public Flux<String> handleErrorWithResume(Flux<String> input, Flux<String> recovery) {
        return input;
    }

    /** Объединяет два потока последовательно: сначала все элементы flux1, затем flux2 */
    public <T> Flux<T> concatFluxes(Flux<T> flux1, Flux<T> flux2) {
        return Flux.empty();
    }

    /**
     * Как expandWithFlatMap, но с сохранением порядка: каждый элемент n
     * разворачивается в (1, 2, ..., n), и результаты идут строго по исходному порядку.
     * Например: [1, 2, 3] → [1, 1, 2, 1, 2, 3].
     */
    public Flux<Integer> transformWithConcatMap(Flux<Integer> input) {
        return input;
    }

    /**
     * Выполняет (потенциально блокирующий) вызов на пуле Schedulers.boundedElastic().
     * Результат должен прийти из потока с именем, начинающимся на "boundedElastic-".
     */
    public Mono<String> runOnBoundedElastic(Supplier<String> blockingTask) {
        return Mono.empty();
    }

    /** Повторно подписывается на источник при ошибке (до retries попыток повтора) */
    public <T> Flux<T> retryOnError(Flux<T> input, int retries) {
        return input;
    }
}

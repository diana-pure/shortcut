package com.multithreading.practice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ReactorTest {

    @Test
    @Timeout(5)
    @DisplayName("Mono.just: createMono('Hello') содержит ровно одно значение 'Hello'")
    void testMonoCreation() {
        ReactiveProcessor processor = new ReactiveProcessor();
        StepVerifier.create(processor.createMono("Hello"))
            .expectNext("Hello")
            .verifyComplete();
    }

    @Test
    @Timeout(5)
    @DisplayName("Flux.just: createFlux(1,2,3,4,5) содержит пять элементов в порядке")
    void testFluxCreation() {
        ReactiveProcessor processor = new ReactiveProcessor();
        StepVerifier.create(processor.createFlux(1, 2, 3, 4, 5))
            .expectNext(1, 2, 3, 4, 5)
            .verifyComplete();
    }

    @Test
    @Timeout(5)
    @DisplayName("map: 'hello', 'world' → 'HELLO', 'WORLD'")
    void testMapOperator() {
        ReactiveProcessor processor = new ReactiveProcessor();
        StepVerifier.create(processor.transformToUpperCase(Flux.just("hello", "world")))
            .expectNext("HELLO", "WORLD")
            .verifyComplete();
    }

    @Test
    @Timeout(5)
    @DisplayName("filter: [1,2,3,4,5,6] → [2,4,6] (только чётные)")
    void testFilterOperator() {
        ReactiveProcessor processor = new ReactiveProcessor();
        StepVerifier.create(processor.filterEvenNumbers(Flux.just(1, 2, 3, 4, 5, 6)))
            .expectNext(2, 4, 6)
            .verifyComplete();
    }

    @Test
    @Timeout(5)
    @DisplayName("flatMap: [1,2,3] разворачивается в 1+2+3=6 элементов")
    void testFlatMap() {
        ReactiveProcessor processor = new ReactiveProcessor();
        StepVerifier.create(processor.expandWithFlatMap(Flux.just(1, 2, 3)))
            .expectNextCount(6)
            .verifyComplete();
    }

    @Test
    @Timeout(5)
    @DisplayName("merge: объединяет 2 потока по 2 элемента → 4 элемента")
    void testMerge() {
        ReactiveProcessor processor = new ReactiveProcessor();
        StepVerifier.create(processor.mergeFluxes(Flux.just("A", "B"), Flux.just("C", "D")))
            .expectNextCount(4)
            .verifyComplete();
    }

    @Test
    @Timeout(5)
    @DisplayName("zip: ['Alice','Bob'] + [25,30] → ['Alice is 25','Bob is 30']")
    void testZip() {
        ReactiveProcessor processor = new ReactiveProcessor();
        StepVerifier.create(processor.zipFluxes(Flux.just("Alice", "Bob"), Flux.just(25, 30)))
            .expectNext("Alice is 25", "Bob is 30")
            .verifyComplete();
    }

    @Test
    @Timeout(5)
    @DisplayName("onErrorReturn: ошибка заменяется fallback-значением в конце потока")
    void testErrorHandlingWithReturn() {
        ReactiveProcessor processor = new ReactiveProcessor();
        Flux<String> flux = Flux.just("A", "B")
            .concatWith(Flux.error(new RuntimeException("ошибка")));

        StepVerifier.create(processor.handleErrorWithReturn(flux, "Error"))
            .expectNext("A", "B", "Error")
            .verifyComplete();
    }

    @Test
    @Timeout(5)
    @DisplayName("onErrorResume: после ошибки поток переключается на recovery")
    void testOnErrorResume() {
        ReactiveProcessor processor = new ReactiveProcessor();
        Flux<String> flux = Flux.just("A")
            .concatWith(Flux.error(new RuntimeException("ошибка")));

        StepVerifier.create(processor.handleErrorWithResume(flux, Flux.just("Recovered")))
            .expectNext("A", "Recovered")
            .verifyComplete();
    }

    @Test
    @Timeout(5)
    @DisplayName("concat: [1,3,5] затем [2,4,6] строго последовательно → 1,3,5,2,4,6")
    void testConcat() {
        ReactiveProcessor processor = new ReactiveProcessor();
        StepVerifier.create(processor.concatFluxes(Flux.just(1, 3, 5), Flux.just(2, 4, 6)))
            .expectNext(1, 3, 5, 2, 4, 6)
            .verifyComplete();
    }

    @Test
    @Timeout(5)
    @DisplayName("concatMap: [1,2,3] разворачивается с сохранением порядка → 1, 1,2, 1,2,3")
    void testConcatMapPreservesOrder() {
        ReactiveProcessor processor = new ReactiveProcessor();
        StepVerifier.create(processor.transformWithConcatMap(Flux.just(1, 2, 3)))
            .expectNext(1, 1, 2, 1, 2, 3)
            .verifyComplete();
    }

    @Test
    @Timeout(5)
    @DisplayName("subscribeOn(boundedElastic): блокирующий вызов выполняется на пуле boundedElastic")
    void testSubscribeOnBoundedElastic() {
        ReactiveProcessor processor = new ReactiveProcessor();
        StepVerifier.create(processor.runOnBoundedElastic(() -> Thread.currentThread().getName()))
            .expectNextMatches(threadName -> threadName.startsWith("boundedElastic-"))
            .verifyComplete();
    }

    @Test
    @Timeout(5)
    @DisplayName("retry: источник падает дважды, успех с третьей попытки при retry(3)")
    void testRetryOnError() {
        ReactiveProcessor processor = new ReactiveProcessor();
        AtomicInteger attempts = new AtomicInteger();
        Flux<String> flaky = Flux.defer(() -> {
            if (attempts.incrementAndGet() < 3) {
                return Flux.error(new RuntimeException("сбой #" + attempts.get()));
            }
            return Flux.just("OK");
        });

        StepVerifier.create(processor.retryOnError(flaky, 3))
            .expectNext("OK")
            .verifyComplete();
        assertEquals(3, attempts.get(), "Должно быть ровно 3 подписки: 1 исходная + 2 повтора");
    }
}

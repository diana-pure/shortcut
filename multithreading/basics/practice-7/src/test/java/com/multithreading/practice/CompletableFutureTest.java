package com.multithreading.practice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CompletableFutureTest {

    @Test
    @Timeout(5)
    @DisplayName("thenApply: цепочка преобразований 'Hello' → 'HELLO WORLD'")
    void testThenApply() throws ExecutionException, InterruptedException {
        AsyncTaskProcessor p = new AsyncTaskProcessor();
        assertEquals("HELLO WORLD", p.createTransformationChain("Hello").get());
    }

    @Test
    @Timeout(5)
    @DisplayName("thenCompose: flatMap 10 → 'Result: 10'")
    void testThenCompose() throws ExecutionException, InterruptedException {
        AsyncTaskProcessor p = new AsyncTaskProcessor();
        assertEquals("Result: 10", p.createComposition(10).get());
    }

    @Test
    @Timeout(5)
    @DisplayName("thenCombine: объединяет 'Hello' и 'World' в 'Hello World'")
    void testThenCombine() throws ExecutionException, InterruptedException {
        AsyncTaskProcessor p = new AsyncTaskProcessor();
        assertEquals("Hello World", p.combineResults("Hello", "World").get());
    }

    @Test
    @Timeout(5)
    @DisplayName("allOf: ожидает завершения всех трёх Future")
    void testAllOf() throws ExecutionException, InterruptedException {
        AsyncTaskProcessor p = new AsyncTaskProcessor();
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> "A");
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> "B");
        CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> "C");

        p.waitForAll(f1, f2, f3).get();

        assertTrue(f1.isDone() && f2.isDone() && f3.isDone());
    }

    @Test
    @Timeout(5)
    @DisplayName("exceptionally: ошибка обрабатывается, возвращается fallback с 'Обработано'")
    void testExceptionally() throws ExecutionException, InterruptedException {
        AsyncTaskProcessor p = new AsyncTaskProcessor();
        String result = p.handleError(true).get();
        assertTrue(result.contains("Обработано"), "Должно содержать 'Обработано', получено: " + result);
    }

    @Test
    @Timeout(5)
    @DisplayName("handle: успешный путь → 'УСПЕХ', ошибочный путь → 'Обработана ошибка'")
    void testHandle() throws ExecutionException, InterruptedException {
        AsyncTaskProcessor p = new AsyncTaskProcessor();
        assertEquals("УСПЕХ", p.handleSuccessOrError(false).get());
        assertEquals("Обработана ошибка", p.handleSuccessOrError(true).get());
    }

    @Test
    @Timeout(5)
    @DisplayName("thenAccept: результат 42 доставляется в consumer")
    void testThenAccept() throws InterruptedException {
        AsyncTaskProcessor p = new AsyncTaskProcessor();
        AtomicInteger received = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(1);

        p.processResult(42, v -> {
            received.set(v);
            latch.countDown();
        });

        latch.await(3, TimeUnit.SECONDS);
        assertEquals(42, received.get());
    }

    @Test
    @Timeout(5)
    @DisplayName("Timeout: get() с тайм-аутом бросает TimeoutException")
    void testTimeout() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try { Thread.sleep(3000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            return "done";
        });
        assertThrows(TimeoutException.class, () -> future.get(200, TimeUnit.MILLISECONDS));
    }
}

package com.multithreading.practice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StarvationSafeRendererTest {

    @Test
    @Timeout(10)
    @DisplayName("StarvationSafeRenderer: рендеры в однопоточном основном пуле не попадают в starvation deadlock")
    void testNoStarvationDeadlock() throws Exception {
        // Основной пул из ОДНОГО потока: если бы подзадачи (header/footer) отправлялись
        // в него же и родитель блокировался на get(), возник бы thread starvation deadlock.
        ExecutorService mainPool = Executors.newSingleThreadExecutor();
        ExecutorService resourcePool = Executors.newFixedThreadPool(2);
        StarvationSafeRenderer renderer = new StarvationSafeRenderer(resourcePool);

        List<Future<String>> pages = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            String body = "<body" + i + ">";
            pages.add(mainPool.submit(() -> renderer.renderPage(body)));
        }

        for (int i = 0; i < 4; i++) {
            // get с таймаутом: дедлок проявился бы зависанием здесь
            assertEquals("[header]<body" + i + ">[footer]", pages.get(i).get(3, TimeUnit.SECONDS));
        }

        mainPool.shutdown();
        resourcePool.shutdown();
        assertTrue(mainPool.awaitTermination(5, TimeUnit.SECONDS), "Основной пул должен завершиться");
        assertTrue(resourcePool.awaitTermination(5, TimeUnit.SECONDS), "Пул ресурсов должен завершиться");
    }

    @Test
    @Timeout(10)
    @DisplayName("StarvationSafeRenderer: header и footer грузятся параллельно (общее время < суммы)")
    void testParallelSubtasks() throws Exception {
        ExecutorService resourcePool = Executors.newFixedThreadPool(2);
        StarvationSafeRenderer renderer = new StarvationSafeRenderer(resourcePool);

        long start = System.nanoTime();
        String page = renderer.renderPage("<body>");
        long elapsedMs = (System.nanoTime() - start) / 1_000_000;

        assertEquals("[header]<body>[footer]", page);
        // Каждая подзадача ~20 мс. При параллельной загрузке общее время заметно меньше 40 мс + запас.
        assertTrue(elapsedMs < 200, "Подзадачи должны грузиться параллельно, фактически: " + elapsedMs + " мс");

        resourcePool.shutdown();
        assertTrue(resourcePool.awaitTermination(5, TimeUnit.SECONDS));
    }
}

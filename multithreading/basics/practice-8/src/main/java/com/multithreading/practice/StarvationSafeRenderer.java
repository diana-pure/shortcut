package com.multithreading.practice;

import java.util.concurrent.ExecutorService;

/**
 * Задание 5. Взаимоблокировка, вызванная голоданием потока (§1 главы 8).
 *
 * В главе показано: если задача, выполняемая в пуле, синхронно ждёт результат
 * ПОДзадач, отправленных в ТОТ ЖЕ пул, может возникнуть thread starvation deadlock —
 * родительская задача занимает поток и ждёт подзадачи, которым уже негде запуститься.
 *
 * Здесь рендеринг страницы устроен так: метод renderPage выполняется как задача
 * ОСНОВНОГО пула, а загрузку header и footer выносит в ОТДЕЛЬНЫЙ пул ресурсов
 * (resourcePool, передан в конструктор). Разделение пулов устраняет дедлок: даже если
 * основной пул однопоточный, подзадачи выполняются в независимом пуле.
 *
 * Реализуйте renderPage(String body):
 * 1. отправьте в resourcePool две подзадачи: load("header") и load("footer")
 *    (получите два Future<String>);
 * 2. верните склейку: header.get() + body + footer.get().
 *
 * Важно: используйте именно resourcePool — НЕ создавайте подзадачи в основном пуле.
 * Метод объявлен throws Exception (его бросают submit/get).
 */
public class StarvationSafeRenderer {

    private final ExecutorService resourcePool;

    public StarvationSafeRenderer(ExecutorService resourcePool) {
        this.resourcePool = resourcePool;
    }

    private String load(String name) {
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return "[" + name + "]";
    }

    public String renderPage(String body) throws Exception {
        return null;
    }
}

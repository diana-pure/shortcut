package com.multithreading.practice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Асинхронное логирование — вынос I/O в отдельный поток (§6).
 *
 * Рабочие потоки лишь кладут сообщение в очередь (log) и не блокируются на «I/O»;
 * отдельный поток-потребитель забирает сообщения и «записывает» их (writeToTarget).
 * Это сокращает время обслуживания запроса и переключения контекста.
 *
 * Реализуйте:
 * - start()  — запустите поток-потребитель, выполняющий consumeLoop();
 * - log(msg) — положите сообщение в queue (producer НЕ должен блокироваться на I/O);
 * - stop()   — КОРРЕКТНО завершите потребителя, дописав ВСЕ уже принятые сообщения
 *              (не теряйте хвост очереди — это главная ловушка);
 * - consumeLoop() — пока работаем ИЛИ очередь не пуста, берите сообщение и пишите его.
 *
 * Подсказка по корректному stop: выставьте флаг running=false и дождитесь worker.join();
 * сам цикл должен дренировать очередь до конца. НЕ прерывайте поток так, чтобы потерять
 * непрочитанные сообщения.
 *
 * Эксперимент: в проде очередь обычно ограничена (bounded) с политикой при переполнении
 * (блокировать producer либо отбрасывать). Здесь очередь не ограничена для простоты теста.
 */
public class AsyncLogger {

    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private final List<String> written = Collections.synchronizedList(new ArrayList<>());
    private volatile boolean running = false;
    private Thread worker;

    public void start() {
    }

    public void log(String message) {
    }

    public void stop() throws InterruptedException {
    }

    public int writtenCount() {
        return written.size();
    }

    public List<String> getWritten() {
        synchronized (written) {
            return new ArrayList<>(written);
        }
    }

    private void consumeLoop() {
    }

    private void writeToTarget(String message) {
        written.add(message); // имитация записи в файл/сеть
    }
}

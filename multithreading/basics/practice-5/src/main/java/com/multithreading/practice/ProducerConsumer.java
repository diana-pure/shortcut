package com.multithreading.practice;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Паттерн "Производитель-Потребитель" на основе BlockingQueue.
 *
 * Реализуйте методы так, чтобы все тесты проходили.
 *
 * Подсказки:
 * - Конструктор принимает BlockingQueue<Integer>
 * - produce(count) — помещает count элементов (Integer) в очередь через put(), инкрементирует счётчик
 * - consume(count) — извлекает count элементов из очереди через take(), инкрементирует счётчик
 * - getProducedCount() — возвращает количество произведённых элементов
 * - getConsumedCount() — возвращает количество потреблённых элементов
 */
public class ProducerConsumer {

    public ProducerConsumer(BlockingQueue<Integer> queue) {
    }

    public void produce(int count) throws InterruptedException {
    }

    public void consume(int count) throws InterruptedException {
    }

    public int getProducedCount() {
        return 0;
    }

    public int getConsumedCount() {
        return 0;
    }
}

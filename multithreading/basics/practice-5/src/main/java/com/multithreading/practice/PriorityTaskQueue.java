package com.multithreading.practice;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * Очередь задач с приоритетом на основе PriorityBlockingQueue.
 *
 * Задачи извлекаются не в порядке поступления, а по приоритету: меньшее
 * значение priority означает более высокий приоритет (1 важнее, чем 5).
 *
 * Ключевые свойства PriorityBlockingQueue, которые проверяют тесты:
 * - очередь НЕограниченная: submit()/put() никогда не блокируется при заполнении;
 * - порядок извлечения определяется compareTo() элементов (внутри — min-heap);
 * - takeNext() блокируется только когда очередь пуста, до появления элемента.
 *
 * Что нужно реализовать:
 * 1. Task implements Comparable<Task>: compareTo сравнивает по priority
 *    (по возрастанию — меньший priority выходит раньше).
 * 2. Методы submit / takeNext / size.
 */
public class PriorityTaskQueue {

    private final PriorityBlockingQueue<Task> queue = new PriorityBlockingQueue<>();

    private static final class Task implements Comparable<Task> {
        private final String name;
        private final int priority;

        Task(String name, int priority) {
            this.name = name;
            this.priority = priority;
        }

        @Override
        public int compareTo(Task other) {
            return 0;
        }
    }

    /**
     * Поместить задачу name с приоритетом priority. Никогда не блокирует.
     */
    public void submit(String name, int priority) {
    }

    /**
     * Блокирующе дождаться и вернуть имя самой приоритетной (наименьший priority) задачи.
     */
    public String takeNext() throws InterruptedException {
        return null;
    }

    /**
     * Текущее число задач в очереди.
     */
    public int size() {
        return 0;
    }
}

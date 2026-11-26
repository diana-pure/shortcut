package com.multithreading.practice;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * Планировщик задач с отложенным стартом на основе DelayQueue.
 *
 * Задача нельзя взять из очереди, пока не истечёт её задержка. Когда несколько
 * задач готовы, первой извлекается та, у которой раньше наступило время
 * готовности (наименьший getDelay).
 *
 * Что нужно реализовать:
 * 1. Внутренний класс DelayedTask implements Delayed:
 *    - хранит имя задачи и абсолютное время готовности (System.nanoTime() + delay);
 *    - getDelay(unit): сколько осталось до готовности (может быть отрицательным);
 *    - compareTo(other): сравнение по оставшейся задержке — это задаёт порядок
 *      извлечения из DelayQueue (раньше готов — раньше извлекаем).
 * 2. Методы schedule / take / poll / size.
 *
 * Подсказки по getDelay/compareTo:
 *   getDelay:   return unit.convert(readyAt - System.nanoTime(), TimeUnit.NANOSECONDS);
 *   compareTo:  return Long.compare(getDelay(NANOSECONDS), other.getDelay(NANOSECONDS));
 */
public class DelayedScheduler {

    private final DelayQueue<DelayedTask> queue = new DelayQueue<>();

    private static class DelayedTask implements Delayed {
        private final String name;
        private final long readyAt; // System.nanoTime() + задержка, в наносекундах

        DelayedTask(String name, long delayMs) {
            this.name = name;
            this.readyAt = 0;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return 0;
        }

        @Override
        public int compareTo(Delayed other) {
            return 0;
        }
    }

    /**
     * Запланировать задачу name на запуск через delayMs миллисекунд.
     */
    public void schedule(String name, long delayMs) {
    }

    /**
     * Блокирующе дождаться ближайшей готовой задачи и вернуть её имя.
     */
    public String take() throws InterruptedException {
        return null;
    }

    /**
     * Неблокирующе вернуть имя готовой задачи или null, если ни одна не готова.
     */
    public String poll() {
        return null;
    }

    /**
     * Текущее число запланированных задач (включая ещё не готовые).
     */
    public int size() {
        return 0;
    }
}

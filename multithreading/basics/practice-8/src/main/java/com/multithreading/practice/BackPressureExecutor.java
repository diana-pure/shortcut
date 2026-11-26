package com.multithreading.practice;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Задание 1. Конструктор ThreadPoolExecutor + политика насыщения (back pressure).
 *
 * В главе 8 показано, что фабрики Executors скрывают конфигурацию пула. Когда нужна
 * ограниченная очередь и контролируемое поведение при перегрузке — ThreadPoolExecutor
 * собирают через конструктор и задают политику насыщения (RejectedExecutionHandler).
 *
 * Реализуйте статический фабричный метод:
 *
 *   static ThreadPoolExecutor create(int poolSize, int queueCapacity)
 *
 * Алгоритм:
 * 1. corePoolSize == maximumPoolSize == poolSize (фиксированный пул).
 * 2. keepAliveTime = 0, TimeUnit.MILLISECONDS.
 * 3. Очередь — ОГРАНИЧЕННАЯ: new ArrayBlockingQueue<>(queueCapacity).
 * 4. Политика насыщения — CallerRunsPolicy: при заполнении очереди задачу выполняет
 *    вызывающий поток (обратное давление, back pressure), задачи не теряются.
 *
 * Подсказка: политику можно передать седьмым аргументом конструктора
 * ThreadPoolExecutor, либо вызвать setRejectedExecutionHandler(...) после создания.
 */
public class BackPressureExecutor {

    private BackPressureExecutor() {
    }

    public static ThreadPoolExecutor create(int poolSize, int queueCapacity) {
        return null;
    }
}

package com.multithreading.practice;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Задание 2. Расширение ThreadPoolExecutor через хуки beforeExecute/afterExecute.
 *
 * ThreadPoolExecutor предоставляет хуки, вызываемые в рабочем потоке ДО и ПОСЛЕ
 * каждой задачи. Так как оба хука выполняются в одном и том же потоке, значение,
 * положенное в ThreadLocal в beforeExecute, доступно в afterExecute — это позволяет
 * измерять длительность каждой задачи (пример TimingThreadPool из главы 8).
 *
 * Класс уже наследует ThreadPoolExecutor с фиксированным пулом — реализуйте:
 *
 * 1. beforeExecute(Thread t, Runnable r):
 *    - вызовите super.beforeExecute(t, r)
 *    - сохраните System.nanoTime() в поле startTime (ThreadLocal<Long>)
 *
 * 2. afterExecute(Runnable r, Throwable t):
 *    - вычислите длительность: System.nanoTime() - startTime.get()
 *    - увеличьте numTasks на 1 и добавьте длительность к totalTimeNanos
 *    - в finally вызовите super.afterExecute(r, t)
 *
 * 3. getMeasuredTaskCount(): верните число завершённых задач (numTasks).
 *
 * 4. getAverageTaskTimeMillis(): среднее время задачи в миллисекундах
 *    (totalTimeNanos / numTasks / 1_000_000.0); при numTasks == 0 верните 0.
 */
public class TimingThreadPool extends ThreadPoolExecutor {

    private final ThreadLocal<Long> startTime = new ThreadLocal<>();
    private final AtomicLong numTasks = new AtomicLong();
    private final AtomicLong totalTimeNanos = new AtomicLong();

    public TimingThreadPool(int poolSize) {
        super(poolSize, poolSize, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
    }

    public long getMeasuredTaskCount() {
        return 0L;
    }

    public double getAverageTaskTimeMillis() {
        return 0.0;
    }
}

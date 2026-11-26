package com.multithreading.practice;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Параллельное суммирование массива long[] с заданным числом потоков.
 *
 * Реализуйте методы:
 * - sequentialSum(long[] array) — последовательная сумма (просто цикл)
 * - parallelSum(long[] array, int threads) — разбивает массив на threads частей,
 *   каждую суммирует в отдельной задаче через ExecutorService, складывает частичные суммы.
 *   Не используйте ForkJoinPool — используйте Executors.newFixedThreadPool(threads) и Future<Long>.
 */
public class ParallelArraySum {

    public long sequentialSum(long[] array) {
        return 0L;
    }

    public long parallelSum(long[] array, int threads) throws InterruptedException {
        return 0L;
    }
}

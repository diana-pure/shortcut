package com.multithreading.practice;

import java.util.concurrent.RecursiveTask;

/**
 * Параллельное суммирование массива long[] через ForkJoinPool.
 *
 * Реализуйте метод compute():
 * - Если длина отрезка [start, end) <= THRESHOLD (используйте 500) — суммируйте последовательно
 * - Иначе — разбейте пополам, fork() левую половину, compute() правую, join() левую, сложите
 *
 * Конструктор принимает: long[] array, int start, int end
 */
public class ArraySumTask extends RecursiveTask<Long> {

    private static final int THRESHOLD = 500;

    public ArraySumTask(long[] array, int start, int end) {
    }

    @Override
    protected Long compute() {
        return 0L;
    }
}

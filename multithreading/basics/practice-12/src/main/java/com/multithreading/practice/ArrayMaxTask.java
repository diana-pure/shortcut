package com.multithreading.practice;

import java.util.concurrent.RecursiveTask;

/**
 * Параллельный поиск максимума в массиве int[] через ForkJoinPool.
 *
 * Реализуйте метод compute():
 * - Если длина отрезка [start, end) <= THRESHOLD (используйте 500) — ищите максимум последовательно
 * - Иначе — разбейте пополам, fork() левую, compute() правую, join() левую, вернуть Math.max
 *
 * Конструктор принимает: int[] array, int start, int end
 */
public class ArrayMaxTask extends RecursiveTask<Integer> {

    private static final int THRESHOLD = 500;

    public ArrayMaxTask(int[] array, int start, int end) {
    }

    @Override
    protected Integer compute() {
        return Integer.MIN_VALUE;
    }
}

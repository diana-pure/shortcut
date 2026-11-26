package com.multithreading.practice;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

/**
 * Параллельный подсчёт слов в массиве строк через ForkJoinPool.
 *
 * Реализуйте метод compute():
 * - Если длина отрезка [start, end) <= THRESHOLD (используйте 10) — считайте слова последовательно:
 *   для каждой строки в [start, end) разбейте по пробелам и посчитайте вхождения
 * - Иначе — разбейте пополам, fork() левую, compute() правую, join() левую,
 *   объедините две Map<String, Integer> (суммируя значения одинаковых ключей)
 *
 * Конструктор принимает: String[] texts, int start, int end
 */
public class WordCountTask extends RecursiveTask<Map<String, Integer>> {

    private static final int THRESHOLD = 10;

    public WordCountTask(String[] texts, int start, int end) {
    }

    @Override
    protected Map<String, Integer> compute() {
        return new HashMap<>();
    }
}

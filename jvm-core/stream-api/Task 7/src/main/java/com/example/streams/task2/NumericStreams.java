package com.example.streams.task2;

import java.util.List;

public class NumericStreams {

    /**
     * Возвращает сумму целых чисел от from до to включительно.
     * Пример: sumRange(1, 5) → 15
     */
    public int sumRange(int from, int to) {
        // TODO: IntStream.rangeClosed(from, to).sum()
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Возвращает среднюю длину слов в списке.
     * Если список пуст — вернуть 0.0.
     */
    public double averageWordLength(List<String> words) {
        // TODO: words.stream().mapToInt(String::length).average().orElse(0.0)
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Возвращает количество различных длин слов.
     * Пример: ["cat", "dog", "elephant"] → 2 (длины 3 и 8)
     */
    public long countDistinctLengths(List<String> words) {
        // TODO: mapToInt(String::length).distinct().count()
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Возвращает список квадратов чисел от 1 до n включительно.
     * Пример: n=4 → [1, 4, 9, 16]
     */
    public List<Integer> generateSquares(int n) {
        // TODO: IntStream.rangeClosed(1, n).map(i -> i * i).boxed().collect(...)
        throw new UnsupportedOperationException("Not implemented");
    }
}

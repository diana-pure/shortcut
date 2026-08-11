package com.example.streams.task2;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;

public class NumericStreams {

    /**
     * Возвращает сумму целых чисел от from до to включительно.
     * Пример: sumRange(1, 5) → 15
     */
    public int sumRange(int from, int to) {
        return IntStream.rangeClosed(from, to).sum();
    }

    /**
     * Возвращает среднюю длину слов в списке.
     * Если список пуст — вернуть 0.0.
     */
    public double averageWordLength(List<String> words) {
        if (isNull(words)) {
            throw new IllegalArgumentException("Words is null");
        }
        return words.stream().map(String::length).collect(Collectors.averagingDouble(Integer::doubleValue));
    }

    /**
     * Возвращает количество различных длин слов.
     * Пример: ["cat", "dog", "elephant"] → 2 (длины 3 и 8)
     */
    public long countDistinctLengths(List<String> words) {
        if (isNull(words)) {
            throw new IllegalArgumentException("Words is null");
        }
        return words.stream().map(String::length).distinct().count();
    }

    /**
     * Возвращает список квадратов чисел от 1 до n включительно.
     * Пример: n=4 → [1, 4, 9, 16]
     */
    public List<Integer> generateSquares(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("N < 0");
        }
        if (n == 0) {
            return Collections.emptyList();
        }
        return IntStream.rangeClosed(1, n).map(x -> x * x).boxed().toList();
    }
}

package com.example.streams.task2;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

public class StreamAggregation {

    /**
     * Возвращает количество положительных чисел (> 0).
     */
    public long countPositive(List<Integer> numbers) {
        if (isNull(numbers)) {
            throw new IllegalArgumentException("Numbers is null");
        }
        return numbers.stream().filter(number -> number > 0).count();
    }

    /**
     * Возвращает сумму всех чисел.
     * Если список пуст — вернуть 0.
     */
    public int sumAll(List<Integer> numbers) {
        if (isNull(numbers)) {
            throw new IllegalArgumentException("Numbers is null");
        }
        return numbers.stream().reduce(0, Integer::sum);
    }

    /**
     * Возвращает минимальный элемент или Optional.empty() если список пуст.
     */
    public Optional<Integer> findMin(List<Integer> numbers) {
        if (isNull(numbers)) {
            throw new IllegalArgumentException("Numbers is null");
        }
        return numbers.stream().min(Integer::compareTo);
    }

    /**
     * Возвращает сумму квадратов всех чисел.
     * Пример: [1, 2, 3] → 1 + 4 + 9 = 14
     */
    public int sumOfSquares(List<Integer> numbers) {
        if (isNull(numbers)) {
            throw new IllegalArgumentException("Numbers is null");
        }
        return numbers.stream().map(number -> number * number).reduce(0, Integer::sum);
    }
}

package com.example.streams.task2;

import java.util.List;
import java.util.Optional;

public class StreamAggregation {

    /**
     * Возвращает количество положительных чисел (> 0).
     */
    public long countPositive(List<Integer> numbers) {
        // TODO: использовать stream().filter().count()
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Возвращает сумму всех чисел.
     * Если список пуст — вернуть 0.
     */
    public int sumAll(List<Integer> numbers) {
        // TODO: использовать stream().reduce(0, Integer::sum)
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Возвращает минимальный элемент или Optional.empty() если список пуст.
     */
    public Optional<Integer> findMin(List<Integer> numbers) {
        // TODO: использовать stream().min(Comparator.naturalOrder())
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Возвращает сумму квадратов всех чисел.
     * Пример: [1, 2, 3] → 1 + 4 + 9 = 14
     */
    public int sumOfSquares(List<Integer> numbers) {
        // TODO: использовать stream().map(n -> n * n).reduce(0, Integer::sum)
        throw new UnsupportedOperationException("Not implemented");
    }
}

package com.example.streams.task2;

import java.util.ArrayList;
import java.util.List;

public class ParallelStreams {

    /**
     * Возвращает сумму всех чисел, используя parallelStream().
     */
    public long parallelSum(List<Long> numbers) {
        // TODO: numbers.parallelStream().reduce(0L, Long::sum)
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Возвращает список слов длиннее minLength, отсортированных для детерминированности.
     * Использовать parallelStream().
     */
    public List<String> parallelFilter(List<String> words, int minLength) {
        // TODO: parallelStream().filter(...).sorted().collect(Collectors.toList())
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Возвращает список чисел строго больше minValue, используя parallelStream() + collect().
     * collect() безопасен в параллельном потоке: каждый поток накапливает результат
     * независимо, а JVM объединяет частичные результаты без race condition.
     *
     * Реализуйте этот метод и сравните надёжность с isSafeToUseSharedList().
     */
    public List<Integer> safeParallelCollect(List<Integer> numbers, int minValue) {
        // TODO: numbers.parallelStream().filter(n -> n > minValue).collect(Collectors.toList())
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * ВНИМАНИЕ: этот метод содержит намеренную ошибку — небезопасное изменение
     * общей коллекции в параллельном потоке.
     *
     * ArrayList не является потокобезопасной структурой данных.
     * При параллельном добавлении элементов возникает race condition:
     * несколько потоков могут одновременно записывать в одно место массива,
     * что приводит к потере элементов или ArrayIndexOutOfBoundsException.
     *
     * ПРАВИЛЬНОЕ решение: использовать collect() вместо forEach() + add().
     */
    public boolean isSafeToUseSharedList(List<Integer> numbers) {
        List<Integer> result = new ArrayList<>();
        numbers.parallelStream().forEach(result::add);  // НЕБЕЗОПАСНО — race condition
        return result.size() == numbers.size();
    }
}

package com.example.streams.task2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class ParallelStreams {

    /**
     * Возвращает сумму всех чисел, используя parallelStream().
     */
    public long parallelSum(List<Long> numbers) {
        if (isNull(numbers)) {
            throw new IllegalArgumentException("Numbers is null");
        }
        return numbers.parallelStream().mapToLong(Long::longValue).sum();
    }

    /**
     * Возвращает список слов длиннее minLength, отсортированных для детерминированности.
     * Использовать parallelStream().
     */
    public List<String> parallelFilter(List<String> words, int minLength) {
        if (isNull(words)) {
            throw new IllegalArgumentException("Words is null");
        }
        return words.parallelStream().filter(word -> word.length() > minLength).sorted().toList();
    }

    /**
     * Возвращает список чисел строго больше minValue, используя parallelStream() + collect().
     * collect() безопасен в параллельном потоке: каждый поток накапливает результат
     * независимо, а JVM объединяет частичные результаты без race condition.
     * <p>
     * Реализуйте этот метод и сравните надёжность с isSafeToUseSharedList().
     */
    public List<Integer> safeParallelCollect(List<Integer> numbers, int minValue) {
        if (isNull(numbers)) {
            throw new IllegalArgumentException("Numbers is null");
        }
        return numbers.parallelStream().filter(number -> number > minValue).toList();
    }

    /**
     * ВНИМАНИЕ: этот метод содержит намеренную ошибку — небезопасное изменение
     * общей коллекции в параллельном потоке.
     * <p>
     * ArrayList не является потокобезопасной структурой данных.
     * При параллельном добавлении элементов возникает race condition:
     * несколько потоков могут одновременно записывать в одно место массива,
     * что приводит к потере элементов или ArrayIndexOutOfBoundsException.
     * <p>
     * ПРАВИЛЬНОЕ решение: использовать collect() вместо forEach() + add().
     */
    public boolean isSafeToUseSharedList(List<Integer> numbers) {
        List<Integer> result = new ArrayList<>();
        numbers.parallelStream().forEach(result::add);  // НЕБЕЗОПАСНО — race condition
        return result.size() == numbers.size();
    }
}

package com.example.streams.task2;

import java.util.List;

public class StreamTransformations {

    /**
     * Возвращает список уникальных чисел, отсортированных по возрастанию.
     */
    public List<Integer> sortedUnique(List<Integer> numbers) {
        // TODO: использовать stream().distinct().sorted().collect()
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Возвращает n самых длинных слов.
     * При одинаковой длине — сортировать по алфавиту.
     */
    public List<String> topNLongest(List<String> words, int n) {
        // TODO: sorted по убыванию длины (затем по алфавиту), затем limit(n)
        // Подсказка: Comparator.comparingInt(String::length).reversed()
        //            .thenComparing(Comparator.naturalOrder())
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Пропускает первые skip элементов, затем берёт не более limit элементов.
     */
    public List<Integer> skipAndLimit(List<Integer> numbers, int skip, int limit) {
        // TODO: использовать stream().skip().limit().collect()
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Возвращает список уникальных чисел в порядке убывания.
     */
    public List<Integer> uniqueSortedDescending(List<Integer> numbers) {
        // TODO: distinct() + sorted(Comparator.reverseOrder())
        throw new UnsupportedOperationException("Not implemented");
    }
}

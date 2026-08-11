package com.example.streams.task2;

import java.util.Comparator;
import java.util.List;

import static java.util.Objects.isNull;

public class StreamTransformations {

    /**
     * Возвращает список уникальных чисел, отсортированных по возрастанию.
     */
    public List<Integer> sortedUnique(List<Integer> numbers) {
        if (isNull(numbers)) {
            throw new IllegalArgumentException("Numbers is null");
        }
        return numbers.stream().sorted().distinct().toList();
    }

    /**
     * Возвращает n самых длинных слов.
     * При одинаковой длине — сортировать по алфавиту.
     */
    public List<String> topNLongest(List<String> words, int n) {
        if (isNull(words)) {
            throw new IllegalArgumentException("Words is null");
        }
        return words.stream()
                .sorted(
                        Comparator
                                .comparingInt(String::length)
                                .reversed()
                                .thenComparing(Comparator.naturalOrder())
                )
                .limit(n)
                .toList();
    }

    /**
     * Пропускает первые skip элементов, затем берёт не более limit элементов.
     */
    public List<Integer> skipAndLimit(List<Integer> numbers, int skip, int limit) {
        if (isNull(numbers)) {
            throw new IllegalArgumentException("Words is null");
        }
        return numbers.stream().skip(skip).limit(limit).toList();
    }

    /**
     * Возвращает список уникальных чисел в порядке убывания.
     */
    public List<Integer> uniqueSortedDescending(List<Integer> numbers) {
        if (isNull(numbers)) {
            throw new IllegalArgumentException("Words is null");
        }
        return numbers.stream().sorted(Comparator.comparingInt(Integer::intValue).reversed()).distinct().toList();
    }
}

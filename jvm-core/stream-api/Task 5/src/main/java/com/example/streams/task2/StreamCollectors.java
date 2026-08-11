package com.example.streams.task2;

import java.util.List;
import java.util.Map;

public class StreamCollectors {

    /**
     * Группирует слова по их длине.
     * Пример: ["cat", "dog", "elephant"] → {3: ["cat", "dog"], 8: ["elephant"]}
     */
    public Map<Integer, List<String>> groupByLength(List<String> words) {
        // TODO: Collectors.groupingBy(String::length)
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Делит числа на чётные (true) и нечётные (false).
     */
    public Map<Boolean, List<Integer>> partitionEvenOdd(List<Integer> numbers) {
        // TODO: Collectors.partitioningBy(n -> n % 2 == 0)
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Объединяет слова через ", ".
     * Пример: ["a", "b", "c"] → "a, b, c"
     */
    public String joinWithComma(List<String> words) {
        // TODO: Collectors.joining(", ")
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Для каждой первой буквы подсчитывает количество слов с этой буквой.
     * Пример: ["apple", "ant", "banana"] → {'a': 2, 'b': 1}
     */
    public Map<Character, Long> countByFirstLetter(List<String> words) {
        // TODO: Collectors.groupingBy(s -> s.charAt(0), Collectors.counting())
        throw new UnsupportedOperationException("Not implemented");
    }
}

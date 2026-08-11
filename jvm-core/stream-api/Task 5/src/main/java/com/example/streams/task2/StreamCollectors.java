package com.example.streams.task2;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class StreamCollectors {

    /**
     * Группирует слова по их длине.
     * Пример: ["cat", "dog", "elephant"] → {3: ["cat", "dog"], 8: ["elephant"]}
     */
    public Map<Integer, List<String>> groupByLength(List<String> words) {
        if (isNull(words)) {
            throw new IllegalArgumentException("Words is null");
        }
        return words.stream().collect(Collectors.groupingBy(String::length));
    }

    /**
     * Делит числа на чётные (true) и нечётные (false).
     */
    public Map<Boolean, List<Integer>> partitionEvenOdd(List<Integer> numbers) {
        if (isNull(numbers)) {
            throw new IllegalArgumentException("Numbers is null");
        }
        return numbers.stream().collect(Collectors.partitioningBy(x -> x % 2 == 0));
    }

    /**
     * Объединяет слова через ", ".
     * Пример: ["a", "b", "c"] → "a, b, c"
     */
    public String joinWithComma(List<String> words) {
        if (isNull(words)) {
            throw new IllegalArgumentException("Words is null");
        }
        return words.stream().collect(Collectors.joining(", "));
    }

    /**
     * Для каждой первой буквы подсчитывает количество слов с этой буквой.
     * Пример: ["apple", "ant", "banana"] → {'a': 2, 'b': 1}
     */
    public Map<Character, Long> countByFirstLetter(List<String> words) {
        if (isNull(words)) {
            throw new IllegalArgumentException("Words is null");
        }
        return words.stream()
                .filter(word -> !isNull(word))
                .filter(word -> !word.isEmpty())
                .filter(word -> !word.isBlank())
                .collect(Collectors.groupingBy(x -> x.charAt(0), Collectors.counting()));
    }
}

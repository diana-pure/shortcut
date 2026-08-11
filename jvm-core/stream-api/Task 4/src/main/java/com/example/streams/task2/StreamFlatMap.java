package com.example.streams.task2;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Objects.isNull;

public class StreamFlatMap {

    /**
     * Возвращает один список, содержащий все числа из всех вложенных списков.
     * Пример: [[1, 2], [3, 4]] → [1, 2, 3, 4]
     */
    public List<Integer> flattenLists(List<List<Integer>> nested) {
        if (isNull(nested)) {
            throw new IllegalArgumentException("Nested lists is null");
        }
        return nested.stream().flatMap(Collection::stream).toList();
    }

    /**
     * Дан список предложений. Вернуть все слова из всех предложений.
     * Разделитель — пробел.
     * Пример: ["hello world", "foo bar"] → ["hello", "world", "foo", "bar"]
     */
    public List<String> extractWords(List<String> sentences) {
        if (isNull(sentences)) {
            throw new IllegalArgumentException("Nested lists is null");
        }
        return sentences.stream().flatMap(x -> Arrays.stream(x.split(" "))).toList();
    }

    /**
     * Возвращает список уникальных чисел из всех вложенных списков,
     * отсортированных по возрастанию.
     */
    public List<Integer> uniqueNumbers(List<List<Integer>> nested) {
        if (isNull(nested)) {
            throw new IllegalArgumentException("Nested lists is null");
        }
        return nested.stream().flatMap(Collection::stream).sorted().distinct().toList();
    }
}

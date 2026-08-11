package com.example.streams.task2;

import java.util.List;

public class StreamFlatMap {

    /**
     * Возвращает один список, содержащий все числа из всех вложенных списков.
     * Пример: [[1, 2], [3, 4]] → [1, 2, 3, 4]
     */
    public List<Integer> flattenLists(List<List<Integer>> nested) {
        // TODO: использовать stream().flatMap(Collection::stream).collect()
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Дан список предложений. Вернуть все слова из всех предложений.
     * Разделитель — пробел.
     * Пример: ["hello world", "foo bar"] → ["hello", "world", "foo", "bar"]
     */
    public List<String> extractWords(List<String> sentences) {
        // TODO: использовать flatMap(s -> Arrays.stream(s.split(" ")))
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Возвращает список уникальных чисел из всех вложенных списков,
     * отсортированных по возрастанию.
     */
    public List<Integer> uniqueNumbers(List<List<Integer>> nested) {
        // TODO: flatMap + distinct() + sorted()
        throw new UnsupportedOperationException("Not implemented");
    }
}

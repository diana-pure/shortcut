package com.example.streams.task2;

import java.util.List;
import java.util.Optional;

public class StreamSearch {

    /**
     * Возвращает первое слово, начинающееся с указанной буквы.
     * Если таких нет — Optional.empty().
     */
    public Optional<String> findFirstStartingWith(List<String> words, char letter) {
        // TODO: stream().filter(s -> s.charAt(0) == letter).findFirst()
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Возвращает true, если в списке есть хотя бы одно отрицательное число.
     */
    public boolean hasNegative(List<Integer> numbers) {
        // TODO: stream().anyMatch(n -> n < 0)
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Возвращает true, если все числа положительные (> 0).
     * Для пустого списка возвращает true.
     */
    public boolean allPositive(List<Integer> numbers) {
        // TODO: stream().allMatch(n -> n > 0)
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Возвращает первое слово длиннее minLength.
     * Если не найдено — возвращает defaultValue.
     */
    public String findFirstOrDefault(List<String> words, int minLength, String defaultValue) {
        // TODO: stream().filter(...).findFirst().orElse(defaultValue)
        throw new UnsupportedOperationException("Not implemented");
    }
}

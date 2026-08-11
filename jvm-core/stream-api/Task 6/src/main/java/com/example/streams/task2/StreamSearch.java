package com.example.streams.task2;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

public class StreamSearch {

    /**
     * Возвращает первое слово, начинающееся с указанной буквы.
     * Если таких нет — Optional.empty().
     */
    public Optional<String> findFirstStartingWith(List<String> words, char letter) {
        if (isNull(words)) {
            return Optional.empty();
        }
        return words.stream()
                .filter(x -> !isNull(x))
                .filter(x -> !x.isEmpty())
                .filter(x -> !x.isBlank())
                .filter(x -> x.charAt(0) == letter)
                .findFirst();
    }

    /**
     * Возвращает true, если в списке есть хотя бы одно отрицательное число.
     */
    public boolean hasNegative(List<Integer> numbers) {
        if (isNull(numbers)) {
            throw new IllegalArgumentException("Numbers is null");
        }
        return numbers.stream().anyMatch(x -> x < 0);
    }

    /**
     * Возвращает true, если все числа положительные (> 0).
     * Для пустого списка возвращает true.
     */
    public boolean allPositive(List<Integer> numbers) {
        if (isNull(numbers)) {
            throw new IllegalArgumentException("Numbers is null");
        }
        return numbers.stream().allMatch(x -> x > 0);
    }

    /**
     * Возвращает первое слово длиннее minLength.
     * Если не найдено — возвращает defaultValue.
     */
    public String findFirstOrDefault(List<String> words, int minLength, String defaultValue) {
        if (isNull(words)) {
            throw new IllegalArgumentException("Words is null");
        }
        return words.stream().filter(x -> x.length() > minLength).findFirst().orElse(defaultValue);
    }
}

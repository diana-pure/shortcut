package com.example.streams.task2;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public class StreamBasics {

    /**
     * Возвращает слова, длина которых строго больше minLength.
     */
    public List<String> filterLongWords(List<String> words, int minLength) {
        if (isNull(words)) {
            throw new IllegalArgumentException("Null words list");
        }
        return words.stream().filter(word -> word.length() > minLength).toList();
    }

    /**
     * Возвращает список слов в верхнем регистре.
     */
    public List<String> toUpperCase(List<String> words) {
        if (isNull(words)) {
            throw new IllegalArgumentException("Null words list");
        }
        return words.stream().filter(Objects::nonNull).map(String::toUpperCase).toList();
    }

    /**
     * Возвращает список длин слов (в том же порядке).
     */
    public List<Integer> wordLengths(List<String> words) {
        if (isNull(words)) {
            throw new IllegalArgumentException("Null words list");
        }
        return words.stream().filter(Objects::nonNull).map(String::length).collect(Collectors.toList());
    }

    /**
     * Возвращает слова длиннее minLength, переведённые в верхний регистр.
     * Объединить filter и map в одну цепочку.
     */
    public List<String> filterAndTransform(List<String> words, int minLength) {
        if (isNull(words)) {
            throw new IllegalArgumentException("Null words list");
        }
        return words.stream().filter(word -> word.length() > minLength).map(String::toUpperCase).toList();
    }
}

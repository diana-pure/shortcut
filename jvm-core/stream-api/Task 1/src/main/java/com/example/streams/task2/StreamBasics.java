package com.example.streams.task2;

import java.util.List;

public class StreamBasics {

    /**
     * Возвращает слова, длина которых строго больше minLength.
     */
    public List<String> filterLongWords(List<String> words, int minLength) {
        // TODO: использовать stream().filter().collect()
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Возвращает список слов в верхнем регистре.
     */
    public List<String> toUpperCase(List<String> words) {
        // TODO: использовать stream().map().collect()
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Возвращает список длин слов (в том же порядке).
     */
    public List<Integer> wordLengths(List<String> words) {
        // TODO: использовать stream().map(String::length).collect()
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Возвращает слова длиннее minLength, переведённые в верхний регистр.
     * Объединить filter и map в одну цепочку.
     */
    public List<String> filterAndTransform(List<String> words, int minLength) {
        // TODO: использовать stream().filter().map().collect()
        throw new UnsupportedOperationException("Not implemented");
    }
}

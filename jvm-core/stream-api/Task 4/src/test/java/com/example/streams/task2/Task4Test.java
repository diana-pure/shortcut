package com.example.streams.task2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Task4Test {

    private StreamFlatMap fm;

    @BeforeEach
    void setUp() {
        fm = new StreamFlatMap();
    }

    // --- flattenLists ---

    @Test
    void flattenLists_basic() {
        List<List<Integer>> nested = List.of(List.of(1, 2), List.of(3, 4), List.of(5));
        assertEquals(List.of(1, 2, 3, 4, 5), fm.flattenLists(nested));
    }

    @Test
    void flattenLists_emptyOuter_returnsEmpty() {
        assertTrue(fm.flattenLists(List.of()).isEmpty());
    }

    @Test
    void flattenLists_someInnerEmpty() {
        List<List<Integer>> nested = List.of(List.of(), List.of(1, 2), List.of());
        assertEquals(List.of(1, 2), fm.flattenLists(nested));
    }

    // --- extractWords ---

    @Test
    void extractWords_basic() {
        List<String> sentences = List.of("hello world", "foo bar");
        assertEquals(List.of("hello", "world", "foo", "bar"), fm.extractWords(sentences));
    }

    @Test
    void extractWords_singleWord() {
        assertEquals(List.of("hello"), fm.extractWords(List.of("hello")));
    }

    @Test
    void extractWords_emptyList_returnsEmpty() {
        assertTrue(fm.extractWords(List.of()).isEmpty());
    }

    @Test
    void extractWords_singleSentenceMultipleWords() {
        List<String> result = fm.extractWords(List.of("a b c"));
        assertEquals(List.of("a", "b", "c"), result);
    }

    // --- uniqueNumbers ---

    @Test
    void uniqueNumbers_removesduplicatesAndSorts() {
        List<List<Integer>> nested = List.of(List.of(3, 1, 2), List.of(2, 4, 1));
        assertEquals(List.of(1, 2, 3, 4), fm.uniqueNumbers(nested));
    }

    @Test
    void uniqueNumbers_emptyOuter_returnsEmpty() {
        assertTrue(fm.uniqueNumbers(List.of()).isEmpty());
    }

    @Test
    void uniqueNumbers_allSame() {
        List<List<Integer>> nested = List.of(List.of(5, 5), List.of(5));
        assertEquals(List.of(5), fm.uniqueNumbers(nested));
    }
}

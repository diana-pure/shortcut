package com.example.streams.task2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class Task5Test {

    private StreamCollectors sc;

    @BeforeEach
    void setUp() {
        sc = new StreamCollectors();
    }

    // --- groupByLength ---

    @Test
    void groupByLength_basic() {
        Map<Integer, List<String>> result = sc.groupByLength(List.of("cat", "dog", "elephant"));
        assertEquals(List.of("cat", "dog"), result.get(3));
        assertEquals(List.of("elephant"), result.get(8));
    }

    @Test
    void groupByLength_emptyList_returnsEmptyMap() {
        assertTrue(sc.groupByLength(List.of()).isEmpty());
    }

    @Test
    void groupByLength_allSameLength() {
        Map<Integer, List<String>> result = sc.groupByLength(List.of("cat", "dog", "fox"));
        assertEquals(1, result.size());
        assertEquals(3, result.get(3).size());
    }

    // --- partitionEvenOdd ---

    @Test
    void partitionEvenOdd_basic() {
        Map<Boolean, List<Integer>> result = sc.partitionEvenOdd(List.of(1, 2, 3, 4, 5));
        assertEquals(List.of(2, 4), result.get(true));
        assertEquals(List.of(1, 3, 5), result.get(false));
    }

    @Test
    void partitionEvenOdd_allEven() {
        Map<Boolean, List<Integer>> result = sc.partitionEvenOdd(List.of(2, 4, 6));
        assertEquals(List.of(2, 4, 6), result.get(true));
        assertTrue(result.get(false).isEmpty());
    }

    @Test
    void partitionEvenOdd_emptyList() {
        Map<Boolean, List<Integer>> result = sc.partitionEvenOdd(List.of());
        assertTrue(result.get(true).isEmpty());
        assertTrue(result.get(false).isEmpty());
    }

    // --- joinWithComma ---

    @Test
    void joinWithComma_basic() {
        assertEquals("a, b, c", sc.joinWithComma(List.of("a", "b", "c")));
    }

    @Test
    void joinWithComma_singleElement() {
        assertEquals("hello", sc.joinWithComma(List.of("hello")));
    }

    @Test
    void joinWithComma_emptyList_returnsEmptyString() {
        assertEquals("", sc.joinWithComma(List.of()));
    }

    // --- countByFirstLetter ---

    @Test
    void countByFirstLetter_basic() {
        Map<Character, Long> result = sc.countByFirstLetter(List.of("apple", "ant", "banana"));
        assertEquals(2L, result.get('a'));
        assertEquals(1L, result.get('b'));
    }

    @Test
    void countByFirstLetter_emptyList_returnsEmptyMap() {
        assertTrue(sc.countByFirstLetter(List.of()).isEmpty());
    }

    @Test
    void countByFirstLetter_allSameLetter() {
        Map<Character, Long> result = sc.countByFirstLetter(List.of("cat", "cow", "crane"));
        assertEquals(1, result.size());
        assertEquals(3L, result.get('c'));
    }
}

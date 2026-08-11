package com.example.streams.task2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Task3Test {

    private StreamTransformations tr;

    @BeforeEach
    void setUp() {
        tr = new StreamTransformations();
    }

    // --- sortedUnique ---

    @Test
    void sortedUnique_removeDuplicatesAndSort() {
        List<Integer> result = tr.sortedUnique(List.of(3, 1, 4, 1, 5, 9, 2, 6, 5, 3));
        assertEquals(List.of(1, 2, 3, 4, 5, 6, 9), result);
    }

    @Test
    void sortedUnique_emptyList_returnsEmpty() {
        assertTrue(tr.sortedUnique(List.of()).isEmpty());
    }

    @Test
    void sortedUnique_alreadyUnique_justSorts() {
        assertEquals(List.of(1, 2, 3), tr.sortedUnique(List.of(3, 1, 2)));
    }

    // --- topNLongest ---

    @Test
    void topNLongest_returnsTopTwo() {
        List<String> words = List.of("cat", "elephant", "ox", "hippopotamus", "dog");
        List<String> result = tr.topNLongest(words, 2);
        assertEquals(List.of("hippopotamus", "elephant"), result);
    }

    @Test
    void topNLongest_tiesByLength_alphabeticalOrder() {
        // All three words have the same length (3); within ties sort alphabetically.
        // Sorted: "bee", "cat", "dog" → take 2 → ["bee", "cat"]
        List<String> result = tr.topNLongest(List.of("dog", "cat", "bee"), 2);
        assertEquals(List.of("bee", "cat"), result);
    }

    @Test
    void topNLongest_nGreaterThanSize_returnsAll() {
        List<String> words = List.of("a", "bb");
        List<String> result = tr.topNLongest(words, 10);
        assertEquals(2, result.size());
    }

    // --- skipAndLimit ---

    @Test
    void skipAndLimit_normalCase() {
        List<Integer> result = tr.skipAndLimit(List.of(1, 2, 3, 4, 5, 6, 7), 2, 3);
        assertEquals(List.of(3, 4, 5), result);
    }

    @Test
    void skipAndLimit_skipAll_returnsEmpty() {
        List<Integer> result = tr.skipAndLimit(List.of(1, 2, 3), 10, 5);
        assertTrue(result.isEmpty());
    }

    @Test
    void skipAndLimit_limitZero_returnsEmpty() {
        List<Integer> result = tr.skipAndLimit(List.of(1, 2, 3), 0, 0);
        assertTrue(result.isEmpty());
    }

    // --- uniqueSortedDescending ---

    @Test
    void uniqueSortedDescending_basic() {
        List<Integer> result = tr.uniqueSortedDescending(List.of(3, 1, 4, 1, 5, 9, 2, 6, 5));
        assertEquals(List.of(9, 6, 5, 4, 3, 2, 1), result);
    }

    @Test
    void uniqueSortedDescending_emptyList_returnsEmpty() {
        assertTrue(tr.uniqueSortedDescending(List.of()).isEmpty());
    }
}

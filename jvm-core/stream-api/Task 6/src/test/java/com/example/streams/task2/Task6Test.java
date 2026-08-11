package com.example.streams.task2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class Task6Test {

    private StreamSearch ss;

    @BeforeEach
    void setUp() {
        ss = new StreamSearch();
    }

    // --- findFirstStartingWith ---

    @Test
    void findFirstStartingWith_found() {
        Optional<String> result = ss.findFirstStartingWith(List.of("cat", "elephant", "ant"), 'a');
        assertTrue(result.isPresent());
        assertEquals("ant", result.get());
    }

    @Test
    void findFirstStartingWith_notFound_returnsEmpty() {
        Optional<String> result = ss.findFirstStartingWith(List.of("cat", "dog"), 'z');
        assertTrue(result.isEmpty());
    }

    @Test
    void findFirstStartingWith_emptyList_returnsEmpty() {
        assertTrue(ss.findFirstStartingWith(List.of(), 'a').isEmpty());
    }

    @Test
    void findFirstStartingWith_returnsFirstMatch() {
        // "apple" comes before "avocado" — should return "apple"
        Optional<String> result = ss.findFirstStartingWith(List.of("apple", "avocado", "banana"), 'a');
        assertTrue(result.isPresent());
        assertEquals("apple", result.get());
    }

    // --- hasNegative ---

    @Test
    void hasNegative_hasNegativeNumber() {
        assertTrue(ss.hasNegative(List.of(1, -2, 3)));
    }

    @Test
    void hasNegative_noNegativeNumbers() {
        assertFalse(ss.hasNegative(List.of(1, 2, 3)));
    }

    @Test
    void hasNegative_emptyList_returnsFalse() {
        assertFalse(ss.hasNegative(List.of()));
    }

    @Test
    void hasNegative_zeroIsNotNegative() {
        assertFalse(ss.hasNegative(List.of(0, 1, 2)));
    }

    // --- allPositive ---

    @Test
    void allPositive_allArePositive() {
        assertTrue(ss.allPositive(List.of(1, 2, 3)));
    }

    @Test
    void allPositive_hasNonPositive() {
        assertFalse(ss.allPositive(List.of(1, 0, 3)));
    }

    @Test
    void allPositive_emptyList_returnsTrue() {
        assertTrue(ss.allPositive(List.of()));
    }

    // --- findFirstOrDefault ---

    @Test
    void findFirstOrDefault_found() {
        String result = ss.findFirstOrDefault(List.of("cat", "elephant", "ox"), 3, "none");
        assertEquals("elephant", result);
    }

    @Test
    void findFirstOrDefault_notFound_returnsDefault() {
        String result = ss.findFirstOrDefault(List.of("cat", "dog"), 10, "none");
        assertEquals("none", result);
    }

    @Test
    void findFirstOrDefault_emptyList_returnsDefault() {
        String result = ss.findFirstOrDefault(List.of(), 3, "fallback");
        assertEquals("fallback", result);
    }
}

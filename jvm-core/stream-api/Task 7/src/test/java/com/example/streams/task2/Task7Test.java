package com.example.streams.task2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Task7Test {

    private NumericStreams ns;

    @BeforeEach
    void setUp() {
        ns = new NumericStreams();
    }

    // --- sumRange ---

    @Test
    void sumRange_oneToFive() {
        assertEquals(15, ns.sumRange(1, 5));
    }

    @Test
    void sumRange_singleNumber() {
        assertEquals(7, ns.sumRange(7, 7));
    }

    @Test
    void sumRange_zeroToZero() {
        assertEquals(0, ns.sumRange(0, 0));
    }

    @Test
    void sumRange_oneToHundred() {
        assertEquals(5050, ns.sumRange(1, 100));
    }

    // --- averageWordLength ---

    @Test
    void averageWordLength_basic() {
        // lengths: 3, 8, 2 → avg = 13/3 ≈ 4.333
        double avg = ns.averageWordLength(List.of("cat", "elephant", "ox"));
        assertEquals(13.0 / 3.0, avg, 0.001);
    }

    @Test
    void averageWordLength_emptyList_returnsZero() {
        assertEquals(0.0, ns.averageWordLength(List.of()), 0.001);
    }

    @Test
    void averageWordLength_singleWord() {
        assertEquals(5.0, ns.averageWordLength(List.of("hello")), 0.001);
    }

    // --- countDistinctLengths ---

    @Test
    void countDistinctLengths_differentLengths() {
        assertEquals(2, ns.countDistinctLengths(List.of("cat", "dog", "elephant")));
    }

    @Test
    void countDistinctLengths_allSameLength() {
        assertEquals(1, ns.countDistinctLengths(List.of("cat", "dog", "fox")));
    }

    @Test
    void countDistinctLengths_emptyList_returnsZero() {
        assertEquals(0, ns.countDistinctLengths(List.of()));
    }

    @Test
    void countDistinctLengths_threeDifferent() {
        assertEquals(3, ns.countDistinctLengths(List.of("a", "bb", "ccc")));
    }

    // --- generateSquares ---

    @Test
    void generateSquares_firstFour() {
        assertEquals(List.of(1, 4, 9, 16), ns.generateSquares(4));
    }

    @Test
    void generateSquares_one() {
        assertEquals(List.of(1), ns.generateSquares(1));
    }

    @Test
    void generateSquares_five() {
        assertEquals(List.of(1, 4, 9, 16, 25), ns.generateSquares(5));
    }
}

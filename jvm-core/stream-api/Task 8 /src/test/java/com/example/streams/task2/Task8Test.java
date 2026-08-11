package com.example.streams.task2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.*;

class Task8Test {

    private ParallelStreams ps;

    @BeforeEach
    void setUp() {
        ps = new ParallelStreams();
    }

    // --- parallelSum ---

    @Test
    void parallelSum_smallList() {
        assertEquals(15L, ps.parallelSum(List.of(1L, 2L, 3L, 4L, 5L)));
    }

    @Test
    void parallelSum_emptyList() {
        assertEquals(0L, ps.parallelSum(List.of()));
    }

    @Test
    void parallelSum_largeList() {
        // Sum of 1..1000 = 500500
        List<Long> numbers = LongStream.rangeClosed(1, 1000)
            .boxed()
            .collect(Collectors.toList());
        assertEquals(500500L, ps.parallelSum(numbers));
    }

    @Test
    void parallelSum_withNegatives() {
        assertEquals(0L, ps.parallelSum(List.of(-3L, -2L, -1L, 1L, 2L, 3L)));
    }

    // --- parallelFilter ---

    @Test
    void parallelFilter_basic() {
        List<String> words = List.of("cat", "elephant", "dog", "hippopotamus", "ox");
        List<String> result = ps.parallelFilter(words, 3);
        // sorted for determinism
        assertEquals(List.of("elephant", "hippopotamus"), result);
    }

    @Test
    void parallelFilter_noneMatch_returnsEmpty() {
        List<String> result = ps.parallelFilter(List.of("a", "bb"), 5);
        assertTrue(result.isEmpty());
    }

    @Test
    void parallelFilter_emptyList_returnsEmpty() {
        assertTrue(ps.parallelFilter(List.of(), 3).isEmpty());
    }

    @Test
    void parallelFilter_isDeteministic() {
        // Run multiple times and expect same result
        List<String> words = List.of("cat", "elephant", "fox", "hippopotamus", "ant");
        List<String> first = ps.parallelFilter(words, 3);
        List<String> second = ps.parallelFilter(words, 3);
        assertEquals(first, second);
    }

    // --- safeParallelCollect ---

    @Test
    void safeParallelCollect_alwaysReturnsExactCount() {
        // 0..999 → filter n > 500 → 501..999 = 499 elements
        List<Integer> numbers = LongStream.range(0, 1000)
            .mapToObj(n -> (int) n)
            .collect(Collectors.toList());

        List<Integer> result = ps.safeParallelCollect(numbers, 500);

        // collect() is thread-safe: no elements are ever lost
        assertEquals(499, result.size());
        assertTrue(result.stream().allMatch(n -> n > 500));
    }

    @Test
    void safeParallelCollect_emptyList() {
        assertTrue(ps.safeParallelCollect(List.of(), 0).isEmpty());
    }

    @Test
    void safeParallelCollect_noneMatch() {
        List<Integer> result = ps.safeParallelCollect(List.of(1, 2, 3), 10);
        assertTrue(result.isEmpty());
    }

    // --- isSafeToUseSharedList ---
    // This test demonstrates the race condition.
    // It may pass sometimes due to non-deterministic scheduling,
    // but students should run it multiple times and observe failures.

    @RepeatedTest(10)
    void isSafeToUseSharedList_demonstratesRaceCondition() {
        // With a large list, race condition is more likely to manifest.
        // The test documents the expected (unsafe) behavior.
        // Students should explain WHY this can fail.
        List<Integer> numbers = LongStream.range(0, 1000)
            .mapToObj(n -> (int) n)
            .collect(Collectors.toList());

        // We don't assert true here — the method is intentionally broken.
        // Just call it to observe behaviour:
        boolean safe = ps.isSafeToUseSharedList(numbers);

        // Document the observation: if safe==false, race condition happened
        // No assertion — test passes either way (behaviour is non-deterministic)
        System.out.printf("isSafeToUseSharedList result: %s (size match: %b)%n",
            safe ? "OK (lucky)" : "DATA LOSS (race condition)", safe);
    }
}

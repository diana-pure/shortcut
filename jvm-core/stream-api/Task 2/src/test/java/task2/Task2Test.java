package task2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Task2Test {

    private StreamAggregation agg;

    @BeforeEach
    void setUp() {
        agg = new StreamAggregation();
    }

    // --- countPositive ---

    @Test
    void countPositive_mixedNumbers() {
        assertEquals(3, agg.countPositive(List.of(-1, 0, 2, 5, -3, 7)));
    }

    @Test
    void countPositive_allNegative_returnsZero() {
        assertEquals(0, agg.countPositive(List.of(-1, -2, -3)));
    }

    @Test
    void countPositive_emptyList_returnsZero() {
        assertEquals(0, agg.countPositive(List.of()));
    }

    // --- sumAll ---

    @Test
    void sumAll_regularList() {
        assertEquals(15, agg.sumAll(List.of(1, 2, 3, 4, 5)));
    }

    @Test
    void sumAll_emptyList_returnsZero() {
        assertEquals(0, agg.sumAll(List.of()));
    }

    @Test
    void sumAll_withNegativeNumbers() {
        assertEquals(0, agg.sumAll(List.of(-3, -2, -1, 1, 2, 3)));
    }

    // --- findMin ---

    @Test
    void findMin_returnsMinimum() {
        Optional<Integer> min = agg.findMin(List.of(3, 1, 4, 1, 5, 9, 2, 6));
        assertTrue(min.isPresent());
        assertEquals(1, min.get());
    }

    @Test
    void findMin_singleElement() {
        Optional<Integer> min = agg.findMin(List.of(42));
        assertTrue(min.isPresent());
        assertEquals(42, min.get());
    }

    @Test
    void findMin_emptyList_returnsEmpty() {
        assertTrue(agg.findMin(List.of()).isEmpty());
    }

    // --- sumOfSquares ---

    @Test
    void sumOfSquares_regularList() {
        assertEquals(14, agg.sumOfSquares(List.of(1, 2, 3)));
    }

    @Test
    void sumOfSquares_emptyList_returnsZero() {
        assertEquals(0, agg.sumOfSquares(List.of()));
    }

    @Test
    void sumOfSquares_singleElement() {
        assertEquals(25, agg.sumOfSquares(List.of(5)));
    }
}

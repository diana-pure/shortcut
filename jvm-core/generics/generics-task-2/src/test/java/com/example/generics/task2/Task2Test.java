package com.example.generics.task2;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class Task2Test {

    @Test
    void getFirstShouldReturnFirstElement() {
        assertEquals("A", CollectionUtils.getFirst(List.of("A","B")));
        assertEquals(1, CollectionUtils.getFirst(List.of(1,2,3)));
    }
}

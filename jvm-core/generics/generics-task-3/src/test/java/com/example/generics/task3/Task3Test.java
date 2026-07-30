package com.example.generics.task3;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class Task3Test {

    @Test
    void sumShouldWorkForDifferentNumberTypes() {
        assertEquals(6.0, NumberUtils.sum(List.of(1,2,3)));
        assertEquals(6.0, NumberUtils.sum(List.of(1.0,2.0,3.0)));
    }
}

package com.example.generics.task4;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class Task4Test {

    @Test
    void copyShouldWorkWithExtendsAndSuper() {
        List<Integer> source = List.of(1,2,3);
        List<Number> dest = new ArrayList<>();

        CopyUtils.copy(source, dest);

        assertEquals(3, dest.size());
    }
}

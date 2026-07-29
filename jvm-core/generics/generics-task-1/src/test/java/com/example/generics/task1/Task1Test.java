package com.example.generics.task1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Task1Test {

    @Test
    void boxShouldStoreValue() {
        Box<String> box = new Box<>("hello");
        assertEquals("hello", box.get());

        box.set("world");
        assertEquals("world", box.get());
    }
}

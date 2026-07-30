package com.example.generics.task4;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public final class CopyUtils {

    private CopyUtils() {
    }

    public static void copy(List<? extends Number> source, List<? super Number> destination) {
        if (isNull(source) || source.isEmpty()) {
            throw new IllegalArgumentException("Source list is null or empty");
        }
        if (isNull(destination)) {
            destination = new ArrayList<>();
        }
        if (!destination.isEmpty()) {
            throw new IllegalArgumentException("Destination list is not empty");
        }
        destination.addAll(source);
    }
}

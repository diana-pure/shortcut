package com.example.generics.task3;

import java.util.List;

import static java.util.Objects.isNull;

public final class NumberUtils {

    private NumberUtils() {
    }

    public static <T extends Number> double sum(List<T> numbers) {
        if (isNull(numbers) || numbers.isEmpty()) {
            throw new IllegalArgumentException();
        }

        double sum = 0;
        for (T e : numbers) {
            sum += e.doubleValue();
        }

        return sum;
    }
}

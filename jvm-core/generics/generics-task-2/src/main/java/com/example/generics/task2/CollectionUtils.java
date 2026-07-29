package com.example.generics.task2;

import java.util.List;

import static java.util.Objects.isNull;

public final class CollectionUtils {

    private CollectionUtils() {
    }

    public static <T> void printArray(T[] array) {
        for (T e : array) {
            System.out.println(e.toString());
        }
    }

    public static <T> T getFirst(List<T> list) {
        if (isNull(list) || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
}

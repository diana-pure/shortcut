package com.multithreading.practice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.Map;
import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ForkJoinPoolTest {

    @Test
    @Timeout(10)
    @DisplayName("ArraySumTask: сумма массива [1..1000] = 500500")
    void testArraySumTask() {
        long[] array = new long[1000];
        for (int i = 0; i < array.length; i++) array[i] = i + 1;

        ForkJoinPool pool = ForkJoinPool.commonPool();
        long result = pool.invoke(new ArraySumTask(array, 0, array.length));

        assertEquals(500_500L, result);
    }

    @Test
    @Timeout(10)
    @DisplayName("ArraySumTask: массив из одного элемента")
    void testArraySumSingleElement() {
        long[] array = {42L};
        long result = ForkJoinPool.commonPool().invoke(new ArraySumTask(array, 0, 1));
        assertEquals(42L, result);
    }

    @Test
    @Timeout(10)
    @DisplayName("ArrayMaxTask: максимум в случайном массиве из 10000 элементов")
    void testArrayMaxTask() {
        int[] array = new int[10_000];
        int expected = Integer.MIN_VALUE;
        for (int i = 0; i < array.length; i++) {
            array[i] = (int) (Math.random() * 1_000_000);
            if (array[i] > expected) expected = array[i];
        }

        int result = ForkJoinPool.commonPool().invoke(new ArrayMaxTask(array, 0, array.length));
        assertEquals(expected, result);
    }

    @Test
    @Timeout(10)
    @DisplayName("ArrayMaxTask: максимум в массиве [5, 3, 9, 1, 7] = 9")
    void testArrayMaxSmall() {
        int[] array = {5, 3, 9, 1, 7};
        int result = ForkJoinPool.commonPool().invoke(new ArrayMaxTask(array, 0, array.length));
        assertEquals(9, result);
    }

    @Test
    @Timeout(10)
    @DisplayName("WordCountTask: подсчёт слов в массиве из 100 строк")
    void testWordCountTask() {
        String[] texts = new String[100];
        for (int i = 0; i < 50; i++) texts[i] = "hello world";
        for (int i = 50; i < 100; i++) texts[i] = "hello java";

        Map<String, Integer> result = ForkJoinPool.commonPool()
            .invoke(new WordCountTask(texts, 0, texts.length));

        assertEquals(100, result.get("hello"), "'hello' встречается 100 раз");
        assertEquals(50, result.get("world"), "'world' встречается 50 раз");
        assertEquals(50, result.get("java"), "'java' встречается 50 раз");
    }

    @Test
    @Timeout(10)
    @DisplayName("ForkJoinPool: кастомный пул с 4 потоками — work-stealing работает")
    void testCustomPoolWorkStealing() {
        long[] array = new long[50_000];
        for (int i = 0; i < array.length; i++) array[i] = 1;

        ForkJoinPool pool = new ForkJoinPool(4);
        long result = pool.invoke(new ArraySumTask(array, 0, array.length));
        pool.shutdown();

        assertEquals(50_000L, result, "Сумма единиц должна равняться длине массива");
    }
}

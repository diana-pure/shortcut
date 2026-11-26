package com.multithreading.practice;

/**
 * Класс с изменяемым полем, доступ к которому не синхронизирован.
 * Задача: пометьте getValue() и setValue() ключевым словом synchronized,
 * чтобы все потоки видели актуальное значение, а не устаревшее.
 */
public class MutableValue {

    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int v) {
        this.value = v;
    }
}

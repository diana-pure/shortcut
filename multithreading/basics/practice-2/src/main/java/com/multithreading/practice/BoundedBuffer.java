package com.multithreading.practice;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Ограниченный буфер (Producer-Consumer) на wait()/notify().
 *
 * Реализуйте конструктор BoundedBuffer(int capacity) и методы:
 *  - void put(T item)  — добавляет элемент; блокируется, пока буфер заполнен (size == capacity);
 *  - T    take()       — забирает элемент (FIFO); блокируется, пока буфер пуст;
 *  - int  size()       — текущее число элементов.
 *
 * Используйте synchronized + wait()/notifyAll() (условие ожидания — в while, не в if).
 * Не используйте классы из java.util.concurrent (Lock, BlockingQueue и т.п.).
 */
public class BoundedBuffer<T> {

    public BoundedBuffer(int capacity) {
    }
}

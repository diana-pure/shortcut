package com.multithreading.practice;

import java.util.concurrent.Semaphore;

/**
 * Ограниченный буфер на массиве фиксированной длины с блокирующими методами put/take,
 * управляемыми парой подсчитывающих семафоров. Это сквозной пример главы 11:
 * на нём гоняются тесты безопасности (контрольная сумма), блокировки и утечек памяти.
 *
 * Инвариант: availableItems.availablePermits() + availableSpaces.availablePermits() == capacity.
 *
 * Реализуйте:
 * - конструктор(capacity): availableItems = Semaphore(0), availableSpaces = Semaphore(capacity),
 *   items = новый массив длины capacity
 * - isEmpty() — true, если доступных для извлечения элементов нет
 * - isFull()  — true, если свободных мест нет
 * - put(E x)  — блокирующий: захватить место (availableSpaces.acquire()), вставить, освободить элемент
 * - take()    — блокирующий: захватить элемент (availableItems.acquire()), извлечь, освободить место
 * - doInsert/doExtract — synchronized, защищают putPosition/takePosition и сам массив
 *
 * ВАЖНО: в doExtract обязательно обнулить ссылку (items[i] = null), иначе массив продолжит
 * удерживать «извлечённый» объект → утечка памяти (проверяется тестом testLeak).
 */
public class BoundedBuffer<E> {

    @SuppressWarnings("unchecked")
    public BoundedBuffer(int capacity) {
    }

    private synchronized void doInsert(E x) {
    }

    private synchronized E doExtract() {
        return null;
    }

    public boolean isEmpty() {
        return true;
    }

    public boolean isFull() {
        return false;
    }

    public void put(E x) throws InterruptedException {
    }

    public E take() throws InterruptedException {
        return null;
    }
}

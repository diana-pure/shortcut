package com.multithreading.practice;

import java.util.concurrent.SynchronousQueue;

/**
 * Прямая передача объектов между потоками на основе SynchronousQueue.
 *
 * SynchronousQueue не имеет буфера вообще: size() всегда 0. Каждая операция
 * вставки должна «встретиться» с операцией извлечения — это паттерн рандеву
 * (hand-off), используемый, например, в Executors.newCachedThreadPool().
 *
 * Свойства, которые проверяют тесты:
 * - tryHandoff() (offer без таймаута) возвращает false, если в этот момент
 *   нет потока, ожидающего в receive();
 * - handoff() (put) блокируется до тех пор, пока другой поток не заберёт элемент;
 * - size() всегда 0 — буфера нет.
 */
public class SynchronousHandoff {

    private final SynchronousQueue<String> channel = new SynchronousQueue<>();

    /**
     * Неблокирующая попытка передать item. Возвращает true только если прямо
     * сейчас есть ожидающий получатель; иначе false (элемент НЕ сохраняется).
     */
    public boolean tryHandoff(String item) {
        return false;
    }

    /**
     * Блокирующе передать item: ждёт, пока другой поток не вызовет receive().
     */
    public void handoff(String item) throws InterruptedException {
    }

    /**
     * Блокирующе получить переданный элемент.
     */
    public String receive() throws InterruptedException {
        return null;
    }

    /**
     * Размер «буфера» — у SynchronousQueue всегда 0.
     */
    public int size() {
        return 0;
    }
}

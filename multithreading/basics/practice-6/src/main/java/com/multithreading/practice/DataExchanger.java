package com.multithreading.practice;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Двусторонний обмен данными между двумя потоками на основе Exchanger.
 *
 * Каждый из двух потоков вызывает exchange(value) и получает объект, переданный партнёром.
 * Первый пришедший блокируется, пока второй не вызовет свой exchange().
 *
 * Реализуйте методы:
 * - exchange(value) — обменяться значением с партнёром (блокируется до прихода второго потока)
 * - exchange(value, timeout, unit) — то же, но с ограничением ожидания;
 *   бросает TimeoutException, если партнёр не пришёл вовремя
 *
 * Используйте java.util.concurrent.Exchanger<T>.
 */
public class DataExchanger<T> {

    public T exchange(T value) throws InterruptedException {
        return null;
    }

    public T exchange(T value, long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
        return null;
    }
}

package com.multithreading.practice;

/**
 * Комбинирование (piggybacking): публикация ОБЫЧНОГО поля через volatile-флаг.
 *
 * ИДЕЯ: поле number — обычное (НЕ volatile). Поле ready — volatile.
 * Запись number = value упорядочена ДО записи ready = true правилом порядка программы.
 * Поток-читатель, увидевший ready == true (Volatile Variable Rule), по транзитивности
 * happens-before гарантированно увидит и актуальное значение number — без объявления
 * number как volatile. Это и есть комбинирование: переиспользуем happens-before флага.
 *
 * ВАЖЕН ПОРЯДОК записей в publish(): сначала number, потом ready.
 * Если поменять местами — гарантия рушится.
 *
 * Реализуйте:
 * - publish(value): number = value; ready = true; (именно в таком порядке)
 * - isReady(): return ready;
 * - read():     return number;
 */
public class PiggybackPublisher {

    private int number;

    private boolean ready;

    public void publish(int value) {
    }

    public boolean isReady() {
        return false;
    }

    public int read() {
        return 0;
    }
}

package com.multithreading.practice;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Задача, корректно обрабатывающая прерывание (Runnable).
 *
 * Реализуйте:
 * - Конструктор принимает AtomicBoolean completed
 * - run() — в цикле спит 100ms, при InterruptedException устанавливает флаг прерывания обратно
 *   и завершает цикл. После нормального завершения цикла (или прерывания) устанавливает completed=true.
 *
 * Подсказка: проверяйте Thread.currentThread().isInterrupted() как условие цикла.
 */
public class InterruptibleTask implements Runnable {

    public InterruptibleTask(AtomicBoolean completed) {
    }

    @Override
    public void run() {
    }
}

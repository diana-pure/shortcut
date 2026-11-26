package com.multithreading.practice;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Реестр слушателей событий на основе CopyOnWriteArrayList.
 *
 * Реализуйте методы так, чтобы все тесты проходили.
 *
 * Подсказки:
 * - Хранит список слушателей: CopyOnWriteArrayList<Consumer<String>>
 * - addListener(listener) — добавляет слушателя
 * - removeListener(listener) — удаляет слушателя
 * - fireEvent(event) — вызывает каждый слушатель с event
 * - getListenerCount() — возвращает количество слушателей
 */
public class EventListenerRegistry {

    public void addListener(Consumer<String> listener) {
    }

    public void removeListener(Consumer<String> listener) {
    }

    public void fireEvent(String event) {
    }

    public int getListenerCount() {
        return 0;
    }
}

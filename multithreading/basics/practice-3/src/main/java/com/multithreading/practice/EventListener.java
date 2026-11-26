package com.multithreading.practice;

/**
 * Слушатель событий. Вспомогательный интерфейс — изменять не нужно.
 */
public interface EventListener {

    void onEvent(Event event);
}

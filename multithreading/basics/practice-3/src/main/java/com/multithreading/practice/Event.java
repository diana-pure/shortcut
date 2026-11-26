package com.multithreading.practice;

/**
 * Простое событие. Вспомогательный класс — изменять не нужно.
 */
public class Event {

    private final String type;

    public Event(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

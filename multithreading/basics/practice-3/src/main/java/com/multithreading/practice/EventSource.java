package com.multithreading.practice;

import java.util.ArrayList;
import java.util.List;

/**
 * Источник событий, на который можно подписать слушателей.
 * Вспомогательный класс — изменять не нужно.
 *
 * Чтобы воспроизвести опасный сценарий this-escape, источник можно настроить
 * на «синхронную нотификацию»: в этом случае он вызывает onEvent() прямо в
 * момент регистрации слушателя. Если слушатель был зарегистрирован из
 * незавершённого конструктора, обработчик увидит объект в недостроенном
 * состоянии (поля ещё null/0).
 */
public class EventSource {

    private final List<EventListener> listeners = new ArrayList<>();
    private final boolean fireOnRegister;
    private final Event eventOnRegister;

    public EventSource() {
        this.fireOnRegister = false;
        this.eventOnRegister = null;
    }

    public EventSource(Event eventOnRegister) {
        this.fireOnRegister = true;
        this.eventOnRegister = eventOnRegister;
    }

    public void registerListener(EventListener listener) {
        listeners.add(listener);
        if (fireOnRegister) {
            // Синхронный вызов в момент регистрации — ловушка для this-escape
            listener.onEvent(eventOnRegister);
        }
    }

    public void fireEvent(Event event) {
        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}

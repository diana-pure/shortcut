package com.skills.gc;

import java.util.ArrayList;
import java.util.List;

/**
 * Простая шина событий для рассылки заказов слушателям.
 *
 * <p>Кажется безобидным — но содержит серьёзную проблему с памятью.
 * Ваша задача: найти утечку, объяснить механизм и исправить.
 *
 * <p>Подсказка: обратите внимание на модификаторы поля {@code listeners}.
 */
public class EventBus {

    // TODO: здесь скрыта утечка памяти. Найдите её и объясните механизм.
    private static final List<OrderListener> listeners = new ArrayList<>();

    /**
     * Подписать слушателя на события о заказах.
     */
    public static void subscribe(OrderListener listener) {
        listeners.add(listener);
    }

    /**
     * Разослать событие всем подписанным слушателям.
     */
    public static void publish(Order order) {
        for (OrderListener listener : listeners) {
            listener.onOrder(order);
        }
    }

    /**
     * TODO (Задание 6): реализовать явную отписку слушателя.
     */
    public static void unsubscribe(OrderListener listener) {
        // TODO: реализовать
        throw new UnsupportedOperationException("Not implemented yet");
    }
}


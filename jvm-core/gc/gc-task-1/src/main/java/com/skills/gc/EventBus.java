package com.skills.gc;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
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
    private static List<SoftReference<OrderListener>> listeners = new ArrayList<>();

    /**
     * Подписать слушателя на события о заказах.
     */
    public static void subscribe(OrderListener listener) {
        listeners.add(new SoftReference<>(listener));
    }

    /**
     * Разослать событие всем подписанным слушателям.
     */
    public static void publish(Order order) {
        for (SoftReference<OrderListener> listener : listeners) {
            OrderListener l = listener.get();
            if(l == null) continue;
            l.onOrder(order);
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


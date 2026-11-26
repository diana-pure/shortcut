package com.multithreading.practice;

/**
 * Задание 5 — устранение дедлока взаимодействующих объектов через открытый вызов (open call).
 *
 * Taxi и Dispatcher вызывают методы друг друга, удерживая собственные замки:
 *   - {@link Dispatcher#getImage()} удерживает замок Dispatcher и вызывает taxi.getLocation()
 *     → захват в порядке Dispatcher → Taxi (этот класс менять НЕЛЬЗЯ);
 *   - наивный setLocation удерживал бы замок Taxi и вызывал dispatcher.notifyAvailable()
 *     → захват в порядке Taxi → Dispatcher.
 * Встречный порядок захвата двух замков → дедлок.
 *
 * ЗАДАЧА: реализуйте setLocation(location) так, чтобы дедлока не было.
 *
 * Подсказка — открытый вызов (open call):
 *   - удерживайте замок this (Taxi) ТОЛЬКО на время изменения собственного состояния:
 *     присвойте location и вычислите, достигнут ли destination (location.equals(destination));
 *   - вызов чужого метода dispatcher.notifyAvailable(this) выполните ВНЕ блока synchronized,
 *     уже отпустив замок Taxi (если пункт назначения достигнут);
 *   - так два замка больше никогда не удерживаются одновременно в обратном порядке.
 */
public class Taxi {

    private Point location;
    private Point destination;
    private final Dispatcher dispatcher;

    public Taxi(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public synchronized Point getLocation() {
        return location;
    }

    public synchronized void setDestination(Point destination) {
        this.destination = destination;
    }

    public void setLocation(Point location) {
    }
}

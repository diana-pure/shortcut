package com.multithreading.practice;

import java.util.ArrayList;
import java.util.List;

/**
 * Слушатель событий, который регистрируется в источнике событий.
 *
 * В текущей реализации присутствует this-escape: слушатель регистрируется
 * прямо в конструкторе, до того как объект полностью сконструирован. Если
 * источник вызовет обработчик синхронно (в момент регистрации), обработчик
 * обратится к полю name, которое ещё не инициализировано (null).
 *
 * Задача:
 * 1. Сделайте конструктор приватным.
 * 2. Перенесите регистрацию слушателя в статический фабричный метод
 *    newInstance(): сначала полностью создайте объект, и только ПОТОМ
 *    регистрируйте слушатель в источнике.
 * 3. Сделайте так, чтобы getReceived() возвращал защитную обёртку, а не
 *    прямую ссылку на внутренний список.
 */
public class SafeListener {

    private final String name;
    private final List<String> received = new ArrayList<>();

    public SafeListener(EventSource source, String name) {
        source.registerListener(new EventListener() {
            @Override
            public void onEvent(Event event) {
                handle(event);
            }
        });
        this.name = name;
    }

    public static SafeListener newInstance(EventSource source, String name) {
        return new SafeListener(source, name);
    }

    private void handle(Event event) {
        received.add(name + ":" + event.getType());
    }

    public String getName() {
        return name;
    }

    public List<String> getReceived() {
        return received;
    }
}

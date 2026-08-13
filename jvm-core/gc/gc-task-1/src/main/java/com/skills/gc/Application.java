package com.skills.gc;

/**
 * Точка входа. Имитирует работу системы обработки заказов.
 *
 * <p>Запускайте с флагами:
 * <pre>
 *   -Xmx64m
 *   -Xlog:gc*
 *   -XX:+HeapDumpOnOutOfMemoryError
 *   -XX:HeapDumpPath=./heap.hprof
 * </pre>
 *
 * <p>Наблюдайте за GC-логами. Через 1–2 минуты приложение упадёт
 * с OutOfMemoryError — хотя «все объекты должны умирать».
 * Ваша задача: понять почему.
 */
public class Application {

    public static void main(String[] args) {
        long orderIdCounter = 0;
        int processorCounter = 0;

        System.out.println("Starting order processing system...");
        System.out.println("Watch GC logs. The app will crash with OOM.");
        System.out.println("Your mission: find out WHY.");
        System.out.println("----------------------------------------------");

        while (true) {
            // Создаём новый процессор для каждого "запроса".
            // Кажется, что он живёт только в этом блоке и потом умрёт.
            OrderProcessor processor = new OrderProcessor("processor-" + processorCounter++);
            EventBus.subscribe(processor);

            // Имитируем обработку нескольких заказов
            for (int i = 0; i < 5; i++) {
                Order order = new Order(orderIdCounter++, "item-" + i, i + 1);
                EventBus.publish(order);
            }

            EventBus.unsubscribe(processor);

            // processor выходит из области видимости — "должен умереть"
            // Но так ли это на самом деле?

            if (processorCounter % 20 == 0) {
                System.out.printf("Created %d processors. Still running...%n", processorCounter);
            }
        }
    }
}


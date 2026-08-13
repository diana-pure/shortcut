package com.skills.gc;

/**
 * Обработчик заказов. Подписывается на EventBus и обрабатывает входящие заказы.
 *
 * <p>Каждый экземпляр содержит внутренний буфер — имитирует реальную нагрузку
 * на память в production-сервисе.
 */
public class OrderProcessor implements OrderListener {

    private final String name;

    // Буфер данных: имитирует кэш, историю заказов или контекст обработки.
    // Намеренно занимает заметный объём памяти, чтобы утечка проявилась быстрее.
    private final byte[] processingBuffer = new byte[512 * 1024]; // 512 KB

    public OrderProcessor(String name) {
        this.name = name;
    }

    @Override
    public void onOrder(Order order) {
        // Имитация обработки заказа
        System.out.printf("[%s] Processing: %s%n", name, order);
    }

    @Override
    public String toString() {
        return "OrderProcessor{name='" + name + "'}";
    }
}


package com.multithreading.practice;

/**
 * Безопасная публикация объектов в многопоточной среде.
 *
 * ПРОБЛЕМА: если объект публикуется через обычное поле без синхронизации,
 * другой поток может увидеть частично инициализированный объект.
 *
 * Реализуйте три метода безопасной публикации:
 *
 * 1. publishWithVolatile(Config config) — сохраняет в volatile поле
 *    getVolatileConfig() — возвращает volatile поле
 *    Поле: private volatile Config volatileConfig;
 *
 * 2. publishWithSynchronized(Config config) — сохраняет под synchronized(this)
 *    getSynchronizedConfig() — читает под synchronized(this)
 *    Поле: private Config syncConfig;
 *
 * 3. getEagerConfig() — возвращает статически инициализированный объект
 *    (eager initialization — безопасна без синхронизации)
 *    Поле: private static final Config EAGER_CONFIG = new Config("eager", 1);
 *
 * Вложенный класс Config уже предоставлен ниже — не изменяйте его.
 */
public class SafePublisher {

    public void publishWithVolatile(Config config) {
    }

    public Config getVolatileConfig() {
        return null;
    }

    public synchronized void publishWithSynchronized(Config config) {
    }

    public synchronized Config getSynchronizedConfig() {
        return null;
    }

    public Config getEagerConfig() {
        return null;
    }

    public static class Config {
        public final String name;
        public final int version;

        public Config(String name, int version) {
            this.name = name;
            this.version = version;
        }
    }
}

package com.multithreading.practice;

/**
 * Ленивая инициализация синглтона.
 *
 * Реализуйте ТРИ варианта:
 *
 * 1. getSynchronizedInstance() — простой synchronized метод
 *    (корректно, но медленно — каждый вызов берёт блокировку)
 *    Поле: private static ExpensiveObject syncInstance;
 *
 * 2. getVolatileDCL() — double-checked locking с volatile
 *    (корректно и быстро)
 *    Поле: private static volatile ExpensiveObject volatileInstance;
 *    Алгоритм: if (null) { synchronized { if (null) { создать } } }
 *
 * 3. getHolderInstance() — инициализация через класс-держатель (Initialization-on-demand holder)
 *    (самый элегантный способ — JVM гарантирует однократную инициализацию)
 *    private static class Holder { static final ExpensiveObject INSTANCE = new ExpensiveObject("holder"); }
 *    return Holder.INSTANCE;
 *
 * Вложенный класс ExpensiveObject уже предоставлен ниже.
 */
public class DoubleCheckedHolder {

    public static synchronized ExpensiveObject getSynchronizedInstance() {
        return null;
    }

    public static ExpensiveObject getVolatileDCL() {
        return null;
    }

    public static ExpensiveObject getHolderInstance() {
        return null;
    }

    public static class ExpensiveObject {
        public final String name;
        public ExpensiveObject(String name) {
            this.name = name;
        }
    }
}

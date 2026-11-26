package com.multithreading.practice;

/**
 * Демонстрация переупорядочивания (PossibleReordering из главы).
 *
 * Два потока БЕЗ синхронизации:
 *   Поток 1: a = 1; x = b;
 *   Поток 2: b = 1; y = a;
 *
 * Кажется, что результат (x=0, y=0) невозможен, но из-за reordering и кэшей он РАЗРЕШЁН JMM:
 * в отсутствие happens-before между потоками действия могут наблюдаться в обратном порядке.
 *
 * Это НЕ задание «сделать правильно» — это задание «увидеть, что бывает неправильно».
 *
 * Реализуйте runOnce():
 * 1. локальные/полевые переменные a, b, x, y сбросить в 0;
 * 2. создать два потока с действиями выше;
 * 3. start() оба, join() оба;
 * 4. вернуть наблюдённую пару new int[]{x, y}.
 *
 * Подсказка: a, b, x, y удобно сделать НЕ volatile полями экземпляра и пересоздавать
 * экземпляр на каждый запуск (тест так и делает).
 */
public class ReorderingDemo {

    private int a;
    private int b;
    private int x;
    private int y;

    public int[] runOnce() throws InterruptedException {
        return new int[]{0, 0};
    }
}

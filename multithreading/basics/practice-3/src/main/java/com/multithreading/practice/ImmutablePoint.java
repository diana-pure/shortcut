package com.multithreading.practice;

/**
 * Точка на плоскости. Должна быть иммутабельной.
 *
 * Задача:
 * 1. Пометьте поля x и y как final.
 * 2. Уберите сеттеры setX() и setY().
 * 3. Убедитесь, что ссылка this не ускользает из конструктора.
 */
public class ImmutablePoint {

    private int x;
    private int y;

    public ImmutablePoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}

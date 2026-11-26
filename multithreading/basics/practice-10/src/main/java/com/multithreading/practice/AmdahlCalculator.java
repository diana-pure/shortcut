package com.multithreading.practice;

/**
 * Вычисление теоретического ускорения по закону Амдала.
 *
 * Закон Амдала: Speedup = 1 / (S + (1 - S) / N)
 * где S — доля последовательного кода, N — количество процессоров.
 */
public class AmdahlCalculator {

    public static double theoreticalSpeedup(double sequentialFraction, int processors) {
        if (sequentialFraction < 0 || sequentialFraction > 1) {
            throw new IllegalArgumentException("sequentialFraction должна быть от 0 до 1");
        }
        if (processors <= 0) {
            throw new IllegalArgumentException("processors должен быть > 0");
        }
        return 1.0 / (sequentialFraction + (1.0 - sequentialFraction) / processors);
    }

    public static double maxSpeedup(double sequentialFraction) {
        if (sequentialFraction == 0) return Double.POSITIVE_INFINITY;
        return 1.0 / sequentialFraction;
    }
}

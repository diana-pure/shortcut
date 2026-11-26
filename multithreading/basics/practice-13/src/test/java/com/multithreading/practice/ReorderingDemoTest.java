package com.multithreading.practice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ReorderingDemoTest {

    /**
     * Наблюдательное задание: запускаем сценарий много раз и собираем статистику исходов.
     * Проверяем только КОРРЕКТНОСТЬ реализации (каждый исход — допустимая пара из {0,1}),
     * а НЕ факт появления (0,0): на сильно упорядоченной x86 он может не встретиться ни разу,
     * на слабых ARM/PowerPC — встречается. Счётчик (0,0) печатается для наглядности.
     */
    @Test
    @Timeout(30)
    @DisplayName("ReorderingDemo: исходы валидны; (0,0) подсчитывается как демонстрация reordering")
    void testReorderingObservable() throws InterruptedException {
        int runs = 100_000;
        long zeroZero = 0;

        for (int i = 0; i < runs; i++) {
            int[] r = new ReorderingDemo().runOnce();
            int x = r[0];
            int y = r[1];

            assertTrue((x == 0 || x == 1) && (y == 0 || y == 1),
                    "Каждое значение обязано быть 0 или 1, получено (" + x + "," + y + ")");

            if (x == 0 && y == 0) {
                zeroZero++;
            }
        }

        System.out.println("ReorderingDemo: (0,0) встретилось " + zeroZero + " раз из " + runs
                + " (0 — нормально для x86, >0 — наблюдаемое переупорядочивание)");

        // Тест проходит независимо от того, появилось ли (0,0): это демонстрация, а не гарантия.
        assertTrue(zeroZero >= 0);
    }
}

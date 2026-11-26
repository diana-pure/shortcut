package com.multithreading.practice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PiggybackPublisherTest {

    /**
     * Повторяем многократно: читатель крутится на volatile-флаге, и как только видит ready,
     * обязан увидеть актуальный number. Без volatile у ready тест может зависнуть (Timeout)
     * или прочитать устаревший 0 — это и демонстрирует роль комбинирования.
     */
    @RepeatedTest(50)
    @Timeout(5)
    @DisplayName("Читатель, увидевший volatile-флаг, видит актуальное обычное поле (piggybacking)")
    void testReaderSeesPublishedValue() throws InterruptedException {
        PiggybackPublisher publisher = new PiggybackPublisher();
        AtomicInteger observed = new AtomicInteger(-1);

        Thread reader = new Thread(() -> {
            while (!publisher.isReady()) {
                // busy-wait на volatile-флаге
            }
            observed.set(publisher.read());
        });

        reader.start();
        publisher.publish(42);
        reader.join(2000);

        assertEquals(42, observed.get(),
                "Увидев ready==true, читатель обязан увидеть number==42 (happens-before через volatile)");
    }
}

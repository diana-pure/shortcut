package com.multithreading.practice;

import jdk.jfr.Recording;
import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordingFile;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Проверяет наличие/отсутствие pinning через JFR-событие jdk.VirtualThreadPinned.
 *
 * Все assert'ы про pinning актуальны только для Java 21–23: в Java 24 (JEP 491)
 * pinning на synchronized устранён, поэтому тесты, ожидающие pinning, пропускаются
 * на 24+ через Assumptions.
 */
class PinningSimulatorTest {

    private static final String PINNED_EVENT = "jdk.VirtualThreadPinned";

    private static final int TASKS = 64;
    private static final int SLEEP_MS = 60;

    private static long recordPinnedEvents(ThrowingRunnable workload) throws Exception {
        Path jfr = Files.createTempFile("pinning-", ".jfr");
        try (Recording recording = new Recording()) {
            recording.enable(PINNED_EVENT).withoutThreshold();
            recording.start();
            workload.run();
            recording.stop();
            recording.dump(jfr);

            List<RecordedEvent> events = RecordingFile.readAllEvents(jfr);
            return events.stream()
                    .filter(e -> e.getEventType().getName().equals(PINNED_EVENT))
                    .count();
        } finally {
            Files.deleteIfExists(jfr);
        }
    }

    private static boolean pinningOnSynchronizedPossible() {
        // JEP 491 (Java 24): pinning на synchronized устранён
        return Runtime.version().feature() < 24;
    }

    @Test
    @Timeout(30)
    @DisplayName("synchronized + блокирующий sleep вызывает pinning виртуальных потоков (Java 21–23)")
    void testSynchronizedCausesPinning() throws Exception {
        Assumptions.assumeTrue(pinningOnSynchronizedPossible(),
                "На Java 24+ pinning на synchronized устранён (JEP 491) — проверка неактуальна");

        PinningSimulator simulator = new PinningSimulator();
        long pinned = recordPinnedEvents(() -> simulator.runWithSynchronized(TASKS, SLEEP_MS));

        assertTrue(pinned > 0,
                "synchronized + блокирующий вызов должен вызывать pinning, но событий jdk.VirtualThreadPinned не зафиксировано");
    }

    @Test
    @Timeout(30)
    @DisplayName("ReentrantLock не вызывает pinning — виртуальный поток демонтируется")
    void testReentrantLockAvoidsPinning() throws Exception {
        PinningSimulator simulator = new PinningSimulator();
        long pinned = recordPinnedEvents(() -> simulator.runWithReentrantLock(TASKS, SLEEP_MS));

        assertEquals(0, pinned,
                "ReentrantLock не должен вызывать pinning (виртуальный поток демонтируется на park())");
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Exception;
    }
}

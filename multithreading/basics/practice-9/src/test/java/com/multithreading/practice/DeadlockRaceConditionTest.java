package com.multithreading.practice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeadlockRaceConditionTest {

    @Test
    @Timeout(10)
    @DisplayName("SafeCounter: 10 потоков × 1000 инкрементов = 10000 (нет race condition)")
    void testRaceConditionFixed() throws InterruptedException {
        SafeCounter counter = new SafeCounter();
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                for (int j = 0; j < 1000; j++) counter.increment();
                latch.countDown();
            });
        }

        latch.await();
        executor.shutdown();

        assertEquals(threadCount * 1000, counter.get());
    }

    @Test
    @Timeout(5)
    @DisplayName("WalletTransfer: однопоточный перевод корректно меняет оба баланса")
    void testWalletTransferCorrectness() {
        Wallet from = new Wallet(100);
        Wallet to = new Wallet(100);

        WalletTransfer.transfer(from, to, 30);

        assertEquals(70, from.balance, "Со счёта-источника должно списаться 30");
        assertEquals(130, to.balance, "На счёт-получатель должно зачислиться 30");
    }

    @Test
    @Timeout(15)
    @DisplayName("WalletTransfer: 100 000 встречных переводов реально переводят деньги без потерь и дедлока")
    void testWalletsNoDeadlock() throws InterruptedException {
        int walletCount = 10;
        int initialBalance = 100;
        List<Wallet> wallets = new ArrayList<>();
        for (int i = 0; i < walletCount; i++) wallets.add(new Wallet(initialBalance));

        // Эталон: те же переводы, посчитанные однопоточно. Каждый перевод атомарен,
        // а сложение коммутативно — поэтому при корректной реализации итог не зависит
        // от порядка выполнения, и конкурентный результат обязан совпасть с эталоном.
        int[] expected = new int[walletCount];
        Arrays.fill(expected, initialBalance);

        ExecutorService executor = Executors.newFixedThreadPool(20);
        int transfers = 100_000;
        for (int i = 0; i < transfers; i++) {
            // Встречные переводы по кольцу: чётные i — вперёд, нечётные — назад.
            // Это провоцирует захват одной пары замков в противоположном порядке.
            int aIdx = i % walletCount;
            int bIdx = (i + 1) % walletCount;
            int fromIdx = (i % 2 == 0) ? aIdx : bIdx;
            int toIdx = (i % 2 == 0) ? bIdx : aIdx;
            expected[fromIdx] -= 1;
            expected[toIdx] += 1;
            Wallet from = wallets.get(fromIdx);
            Wallet to = wallets.get(toIdx);
            executor.execute(() -> WalletTransfer.transfer(from, to, 1));
        }

        executor.shutdown();
        boolean finished = executor.awaitTermination(10, TimeUnit.SECONDS);

        assertTrue(finished, "Пул должен завершиться — упорядочивание замков исключает дедлок");

        // Сценарий обязан реально перемещать деньги, иначе тест ничего не проверяет
        // (пустая реализация transfer() оставила бы все балансы равными initialBalance).
        assertFalse(Arrays.stream(expected).allMatch(b -> b == initialBalance),
                "Эталон должен отличаться от начального состояния — иначе сценарий тривиален");

        // Точное совпадение с эталоном доказывает: переводы реально выполнены
        // и нет потерянных обновлений (race condition при списании/зачислении).
        int[] actual = wallets.stream().mapToInt(w -> w.balance).toArray();
        assertArrayEquals(expected, actual,
                "Балансы должны совпасть с однопоточным эталоном — расхождение означает потерянные обновления");

        int total = Arrays.stream(actual).sum();
        assertEquals(walletCount * initialBalance, total, "Суммарный баланс должен сохраниться");
    }

    @Test
    @Timeout(20)
    @DisplayName("TryLockTransfer: 50 000 встречных переводов с tryLock+backoff завершаются все")
    void testTryLockTransferNoDeadlock() throws InterruptedException {
        List<LockedAccount> accounts = new ArrayList<>();
        for (int i = 0; i < 10; i++) accounts.add(new LockedAccount(100));

        ExecutorService executor = Executors.newFixedThreadPool(20);
        int transfers = 50_000;
        AtomicInteger succeeded = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(transfers);

        for (int i = 0; i < transfers; i++) {
            LockedAccount a = accounts.get(i % accounts.size());
            LockedAccount b = accounts.get((i + 1) % accounts.size());
            LockedAccount from = (i % 2 == 0) ? a : b;
            LockedAccount to = (i % 2 == 0) ? b : a;
            executor.execute(() -> {
                try {
                    if (TryLockTransfer.transfer(from, to, 1, 200)) succeeded.incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }

        boolean done = latch.await(17, TimeUnit.SECONDS);
        executor.shutdown();

        assertTrue(done, "tryLock+backoff должен исключить дедлок — все задачи завершаются");
        assertEquals(transfers, succeeded.get(), "Повтор с backoff обязан довести каждый перевод до успеха");
        int total = accounts.stream().mapToInt(acc -> acc.balance).sum();
        assertEquals(1000, total, "Суммарный баланс должен остаться 1000");
    }

    @Test
    @Timeout(10)
    @DisplayName("LivelockResolver: оба философа доедают свою норму благодаря случайной задержке")
    void testLivelockResolvedWithBackoff() throws InterruptedException {
        int mealsPerDiner = 2000;

        int[] eaten = LivelockResolver.dine(mealsPerDiner);

        assertArrayEquals(new int[]{mealsPerDiner, mealsPerDiner}, eaten,
                "Оба философа должны достичь нормы — backoff разрывает livelock");
    }

    @Test
    @Timeout(15)
    @DisplayName("Open call: setLocation вне блокировки исключает дедлок Taxi↔Dispatcher")
    void testOpenCallNoDeadlock() throws InterruptedException {
        Dispatcher dispatcher = new Dispatcher();
        List<Taxi> taxis = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Taxi taxi = new Taxi(dispatcher);
            taxi.setDestination(new Point(i, i));
            dispatcher.register(taxi);
            taxis.add(taxi);
        }

        ExecutorService executor = Executors.newFixedThreadPool(8);
        int iterations = 50_000;

        // Сеттеры: захватывают замок Taxi и (потенциально) замок Dispatcher.
        for (int i = 0; i < iterations; i++) {
            Taxi taxi = taxis.get(i % taxis.size());
            Point point = new Point(i % 100, i % 100);
            executor.execute(() -> taxi.setLocation(point));
        }
        // Читатели: захватывают замок Dispatcher и замок Taxi — в противоположном порядке.
        for (int i = 0; i < iterations; i++) {
            executor.execute(dispatcher::getImage);
        }

        executor.shutdown();
        boolean finished = executor.awaitTermination(12, TimeUnit.SECONDS);

        assertTrue(finished, "Открытый вызов должен исключить дедлок между Taxi и Dispatcher");
        for (Taxi taxi : taxis) {
            assertNotNull(taxi.getLocation(), "После setLocation координата должна быть установлена");
        }
    }
}

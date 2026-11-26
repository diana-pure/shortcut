package com.multithreading.practice;

/**
 * Задание 3 — предотвращение дедлока через tryLock с тайм-аутом.
 *
 * В отличие от задания 2 (где порядок замков фиксируется заранее), здесь применяется
 * другой приём: каждый счёт несёт свой ReentrantLock (см. {@link LockedAccount}),
 * а перевод пытается захватить обе блокировки с тайм-аутом и отступает при неудаче.
 *
 * ЗАДАЧА: реализуйте transfer(from, to, amount, timeoutMs) так, чтобы:
 *   1. метод возвращал true, когда перевод выполнен (from -= amount, to += amount);
 *   2. при 50 000 встречных параллельных переводах не возникало дедлока;
 *   3. КАЖДЫЙ перевод в итоге доводился до успеха (тест требует ровно 50 000 успехов).
 *
 * Подсказка — tryLock(timeout) + backoff + retry:
 *   - попытайтесь from.lock.tryLock(timeoutMs); при успехе — to.lock.tryLock(timeoutMs);
 *   - если вторую блокировку взять не удалось, освободите первую (finally!),
 *     подождите небольшую СЛУЧАЙНУЮ задержку (ThreadLocalRandom) и повторите попытку;
 *   - цикл ожидания разрывается, потому что один из потоков отступает и освобождает замок.
 *   - всегда снимайте блокировки в блоке finally.
 */
public class TryLockTransfer {

    public static boolean transfer(LockedAccount from, LockedAccount to, int amount, long timeoutMs)
            throws InterruptedException {
        return false;
    }
}

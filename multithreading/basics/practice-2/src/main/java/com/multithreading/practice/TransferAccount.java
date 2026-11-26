package com.multithreading.practice;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Счёт с переводом средств, защищённым от дедлока через tryLock().
 *
 * Контракт (см. тест SynchronizationTest):
 * - new TransferAccount(long initialBalance) — создать счёт с начальным балансом;
 * - long getBalance() — текущий баланс;
 * - boolean transferTo(TransferAccount to, long amount) — перевести amount на счёт to.
 *
 * Требования к реализации:
 * - использовать ReentrantLock и tryLock() с отступлением (НЕ lock()), чтобы
 *   встречные переводы (A->B и B->A) не приводили к дедлоку при захвате двух замков;
 * - перевод должен быть атомарным: сумма всех балансов сохраняется при любой конкуренции;
 * - при недостатке средств перевод не выполняется и возвращает false.
 */
public class TransferAccount {

    private final ReentrantLock lock = new ReentrantLock();

}

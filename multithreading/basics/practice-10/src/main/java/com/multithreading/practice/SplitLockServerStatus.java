package com.multithreading.practice;

import java.util.HashSet;
import java.util.Set;

/**
 * Статус сервера с разделением блокировки (lock splitting).
 *
 * Два независимых набора состояния — users и queries — должны защищаться РАЗНЫМИ
 * блокировками, а не одной общей. Тогда добавление пользователя и добавление запроса
 * не конкурируют друг с другом и могут идти параллельно.
 *
 * Контракт (важно для тестов): синхронизируйтесь на САМИХ коллекциях —
 * synchronized(users) для операций с users и synchronized(queries) для операций с queries.
 * НЕ используйте общий монитор this.
 *
 * Реализуйте addUser/addQuery/userCount/queryCount согласно этому контракту.
 */
public class SplitLockServerStatus {

    public final Set<String> users = new HashSet<>();
    public final Set<String> queries = new HashSet<>();

    public void addUser(String user) {
    }

    public void addQuery(String query) {
    }

    public int userCount() {
        return 0;
    }

    public int queryCount() {
        return 0;
    }
}

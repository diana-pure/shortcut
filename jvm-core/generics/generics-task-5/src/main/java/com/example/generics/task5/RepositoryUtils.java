package com.example.generics.task5;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

/**
 * Утилиты для задания.
 *
 * <p>Важно: специально оставлены TODO для студента.
 */
public final class RepositoryUtils {

    private RepositoryUtils() {
    }

    /**
     * Демонстрация проблемы {@code List<?>}.
     *
     * <p>Внутри метода нельзя безопасно:
     * <ul>
     *   <li>читать элементы как {@code User}, потому что тип неизвестен;</li>
     *   <li>добавлять элементы (кроме {@code null}), потому что компилятор не знает точный тип {@code ?}.</li>
     * </ul>
     */
    public static void saveAllWildcard(Repository<User> repo, List<?> entities) {
        if (repo == null) throw new IllegalArgumentException("repo must not be null");
        if (entities == null) throw new IllegalArgumentException("entities must not be null");

        // Примеры, которые НЕ компилируются (оставлены как комментарии):
//         repo.save(entities.get(0));            // Object cannot be converted to User
//         entities.add(new User("x", "y"));      // capture of ?

        // Этот метод годится разве что для логирования:
        System.out.println("saveAllWildcard: " + entities.size() + " elements");
    }

    /**
     * TODO: Реализуйте корректный метод сохранения, который:
     * <ul>
     *   <li>принимает список {@code User} и его потомков (например, {@code AdminUser});</li>
     *   <li>сохраняет всех элементов в {@code repo};</li>
     * </ul>
     *
     * <p>Подсказка: список здесь "producer" (мы читаем из него элементы).
     */
    public static void saveAll(Repository<User> repo, List<? extends User> entities) {
        if (isNull(entities)) throw new IllegalArgumentException("entities must not be null");
        for (User e : entities) {
            repo.save(e);
        }
    }

    /**
     * TODO: Верните первого пользователя из списка.
     *
     * <p>Подсказка: если параметр должен принимать {@code List<User>} и {@code List<AdminUser>},
     * то используйте wildcard с {@code extends}.
     */
    public static User getFirstUser(List<? extends User> users) {
        if (isNull(users) || users.isEmpty()) {
            throw new IllegalArgumentException("users list is null or empty");
        }
        return users.get(0);
    }

    /**
     * TODO: Добавьте "дефолтного" пользователя в список-приёмник.
     *
     * <p>Подсказка: если метод ДОБАВЛЯЕТ {@code User} в коллекцию, параметр должен быть
     * "consumer", то есть wildcard с {@code super}.
     *
     * <p>Метод должен позволять передать и {@code List<User>}, и {@code List<Object>}.
     */
    public static void addDefaultUser(List<? super User> sink) {
        if (isNull(sink)) {
            sink = new ArrayList<>();
        }
        sink.add(new User("default", "default"));
    }
}

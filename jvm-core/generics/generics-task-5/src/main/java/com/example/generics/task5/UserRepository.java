package com.example.generics.task5;

import java.util.HashMap;
import java.util.Map;

/**
 * Простейшая реализация репозитория: хранение в памяти.
 */
public final class UserRepository implements Repository<User> {

    private final Map<String, User> storage = new HashMap<>();

    @Override
    public void save(User entity) {
        if (entity == null) {
            throw new IllegalArgumentException("entity must not be null");
        }
        storage.put(entity.getId(), entity);
    }

    @Override
    public User findById(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id must not be blank");
        }
        return storage.get(id);
    }
}

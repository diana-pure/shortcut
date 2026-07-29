package com.example.generics.task5;

/**
 * Упрощённый репозиторий для демонстрации инвариантности и wildcard'ов.
 *
 * @param <T> тип сущности
 */
public interface Repository<T> {
    void save(T entity);
    T findById(String id);
}

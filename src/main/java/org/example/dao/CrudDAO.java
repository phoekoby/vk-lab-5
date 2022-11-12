package org.example.dao;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

public interface CrudDAO<V, K> {
    Collection<V> getAll();

    Optional<V> getById(@NotNull K id);

    void delete(@NotNull K id);

    V update(@NotNull V value);

    V save(@NotNull V value);

    Collection<V> saveAll(@NotNull Collection<V> values);
}

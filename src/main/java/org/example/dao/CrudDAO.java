package org.example.dao;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public interface CrudDAO<V, K> {
    Collection<V> getAll();

    Optional<V> getById(@NotNull K id);

    void delete(@NotNull K id);

    V update(@NotNull V value);

    V save(@NotNull V value);

    default Collection<V> saveAll(@NotNull Collection<V> values){
        return values
                .stream()
                .map(this::save)
                .collect(Collectors.toList());
    }
}

package org.example.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDAO<V, K> {
    List<V> getAll();

    Optional<V> getById(K id);

    void delete(K id);

    V update(V value);

    V save(V value);
}

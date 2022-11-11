package org.example.dao.impl;

import org.example.dao.ProductDAO;
import org.example.entity.Product;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ProductDAOImpl implements ProductDAO {
    @Override
    public List<Product> getAll() {
        return null;
    }

    @Override
    public Optional<Product> getById(@NotNull Long id) {
        return Optional.empty();
    }

    @Override
    public void delete(@NotNull Long id) {

    }

    @Override
    public Product update(@NotNull Product value) {
        return null;
    }

    @Override
    public Product save(@NotNull Product value) {
        return null;
    }

    @Override
    public Collection<Product> saveAll(@NotNull Collection<Product> values) {
        return null;
    }


}

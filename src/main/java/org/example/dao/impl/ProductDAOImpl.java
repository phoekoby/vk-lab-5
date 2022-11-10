package org.example.dao.impl;

import org.example.dao.ProductDAO;
import org.example.entity.Product;

import java.util.List;
import java.util.Optional;

public class ProductDAOImpl implements ProductDAO {
    @Override
    public List<Product> getAll() {
        return null;
    }

    @Override
    public Optional<Product> getById(Long id) {
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Product update(Product value) {
        return null;
    }

    @Override
    public Product save(Product value) {
        return null;
    }
}

package org.example.dao.impl;

import org.example.dao.InvoiceDAO;
import org.example.entity.Invoice;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class InvoiceDAOImpl implements InvoiceDAO {
    @Override
    public List<Invoice> getAll() {
        return null;
    }

    @Override
    public Optional<Invoice> getById(@NotNull Long id) {
        return Optional.empty();
    }

    @Override
    public void delete(@NotNull Long id) {

    }

    @Override
    public Invoice update(@NotNull Invoice value) {
        return null;
    }

    @Override
    public Invoice save(@NotNull Invoice value) {
        return null;
    }

    @Override
    public Collection<Invoice> saveAll(@NotNull Collection<Invoice> values) {
        return null;
    }
}

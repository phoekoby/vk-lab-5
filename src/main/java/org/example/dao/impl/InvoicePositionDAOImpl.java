package org.example.dao.impl;

import org.example.dao.InvoicePositionDAO;
import org.example.entity.InvoicePosition;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class InvoicePositionDAOImpl implements InvoicePositionDAO {
    @Override
    public List<InvoicePosition> getAll() {
        return null;
    }

    @Override
    public Optional<InvoicePosition> getById(@NotNull Long id) {
        return Optional.empty();
    }

    @Override
    public void delete(@NotNull Long id) {

    }

    @Override
    public InvoicePosition update(@NotNull InvoicePosition value) {
        return null;
    }

    @Override
    public InvoicePosition save(@NotNull InvoicePosition value) {
        return null;
    }

    @Override
    public Collection<InvoicePosition> saveAll(@NotNull Collection<InvoicePosition> values) {
        return null;
    }
}

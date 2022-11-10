package org.example.dao.impl;

import org.example.dao.InvoicePositionDAO;
import org.example.entity.InvoicePosition;

import java.util.List;
import java.util.Optional;

public class InvoicePositionDAOImpl implements InvoicePositionDAO {
    @Override
    public List<InvoicePosition> getAll() {
        return null;
    }

    @Override
    public Optional<InvoicePosition> getById(Long id) {
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public InvoicePosition update(InvoicePosition value) {
        return null;
    }

    @Override
    public InvoicePosition save(InvoicePosition value) {
        return null;
    }
}

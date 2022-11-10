package org.example.dao.impl;

import org.example.dao.InvoiceDAO;
import org.example.entity.Invoice;

import java.util.List;
import java.util.Optional;

public class InvoiceDAOImpl implements InvoiceDAO {
    @Override
    public List<Invoice> getAll() {
        return null;
    }

    @Override
    public Optional<Invoice> getById(Long id) {
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Invoice update(Invoice value) {
        return null;
    }

    @Override
    public Invoice save(Invoice value) {
        return null;
    }
}

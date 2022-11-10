package org.example.dao.impl;

import org.example.dao.OrganizationDAO;
import org.example.entity.Organization;

import java.util.List;
import java.util.Optional;

public class OrganizationDAOImpl implements OrganizationDAO {
    @Override
    public List<Organization> getAll() {
        return null;
    }

    @Override
    public Optional<Organization> getById(Long id) {
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Organization update(Organization value) {
        return null;
    }

    @Override
    public Organization save(Organization value) {
        return null;
    }
}

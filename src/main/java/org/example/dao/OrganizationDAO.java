package org.example.dao;

import org.example.entity.Organization;

import java.util.List;

public interface OrganizationDAO extends CrudDAO<Organization, Long> {

    List<Organization> findFirst10OrganizationsByProduct(Long productId);

    List<Organization> findOrganizationAmountProductMoreThanValue(int value);
}

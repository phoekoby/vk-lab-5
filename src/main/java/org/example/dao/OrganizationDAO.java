package org.example.dao;

import org.example.entity.Organization;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface OrganizationDAO extends CrudDAO<Organization, Long> {

    List<Organization> findFirst10OrganizationsByProduct(@NotNull Long productId);

    List<Organization> findOrganizationAmountProductMoreThanValue(@NotNull Map<Long, Integer> productsWithLimits);
}

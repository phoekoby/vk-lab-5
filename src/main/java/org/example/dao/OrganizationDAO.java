package org.example.dao;

import org.example.entity.OrganizationDTO;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public interface OrganizationDAO extends CrudDAO<OrganizationDTO, Long> {

    List<OrganizationDTO> findFirst10OrganizationsByProduct(@NotNull Long productId);

    List<OrganizationDTO> findOrganizationAmountProductMoreThanValue(@NotNull Map<Long, Integer> productsWithLimits);
}

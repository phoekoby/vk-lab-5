package org.example.dto;

import org.example.entity.Organization;
import org.example.entity.Product;

import java.util.List;
import java.util.Map;

public class PeriodAmountAndSumProductDTO {
    Map<Organization, List<Product>> products;
}

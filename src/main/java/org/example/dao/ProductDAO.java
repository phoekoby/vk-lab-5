package org.example.dao;

import org.example.dto.PeriodAmountSumAndResultProductDTO;
import org.example.entity.Organization;
import org.example.entity.Product;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public interface ProductDAO extends CrudDAO<Product, Long> {
    PeriodAmountSumAndResultProductDTO getAmountAndSumPerDayAndResultForPeriod(Date floor, Date roof);

    Map<Product, Double> getAveragePriceForProductsInPeriod(Date floor, Date roof);

    Map<Organization, List<Product>> getDeliveredProductsByOrganizationInPeriod(Date floor, Date roof);
}

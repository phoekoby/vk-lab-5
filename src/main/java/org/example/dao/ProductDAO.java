package org.example.dao;

import org.example.dto.PeriodAmountSumAndResultProductDTO;
import org.example.entity.OrganizationDTO;
import org.example.entity.ProductDTO;

import java.sql.Date;
import java.util.List;
import java.util.Map;

public interface ProductDAO extends CrudDAO<ProductDTO, Long> {
    PeriodAmountSumAndResultProductDTO getAmountAndSumPerDayAndResultForPeriod(Date floor, Date roof);

    Map<ProductDTO, Double> getAveragePriceForProductsInPeriod(Date floor, Date roof);

    Map<OrganizationDTO, List<ProductDTO>> getDeliveredProductsByOrganizationInPeriod(Date floor, Date roof);
}

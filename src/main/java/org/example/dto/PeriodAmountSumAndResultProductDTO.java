package org.example.dto;

import lombok.Data;
import org.example.entity.Product;

import java.sql.Date;
import java.util.Map;

@Data
public class PeriodAmountSumAndResultProductDTO {
    private final Map<Date, Map<Product, AmountSumDTO>> productsPerDay;
    private final Map<Product, AmountSumDTO> amountSumResultForPeriod;
}

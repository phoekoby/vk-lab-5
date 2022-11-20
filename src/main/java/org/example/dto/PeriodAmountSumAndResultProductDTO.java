package org.example.dto;

import lombok.Data;
import org.example.entity.ProductDTO;

import java.sql.Date;
import java.util.Map;

@Data
public class PeriodAmountSumAndResultProductDTO {
    private final Map<Date, Map<ProductDTO, AmountSumDTO>> productsPerDay;
    private final Map<ProductDTO, AmountSumDTO> amountSumResultForPeriod;
}

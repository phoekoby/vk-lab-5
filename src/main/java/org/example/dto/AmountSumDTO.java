package org.example.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AmountSumDTO implements Serializable {
    private final Integer amount;
    private final Double sum;
}

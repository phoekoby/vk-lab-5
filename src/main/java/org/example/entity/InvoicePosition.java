package org.example.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public final class InvoicePosition implements Serializable {
    private final Long id;
    private final Double price;
    private final Integer amount;
    private final Long productId;
    private final Long invoiceId;
}

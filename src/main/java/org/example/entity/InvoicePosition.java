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


    public InvoicePosition(Long id, Double price, Integer amount, Long productId, Long invoiceId) {
        this.id = id;
        this.price = price;
        this.amount = amount;
        this.productId = productId;
        this.invoiceId = invoiceId;
    }

    public InvoicePosition(Double price, Integer amount, Long productId, Long invoiceId) {
        this.id = null;
        this.price = price;
        this.amount = amount;
        this.productId = productId;
        this.invoiceId = invoiceId;
    }
}

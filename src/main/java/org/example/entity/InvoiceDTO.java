package org.example.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
public final class InvoiceDTO implements Serializable {
    private final Long id;
    private final Long number;
    private final Date invoiceDate;
    private final Long senderId;

    public InvoiceDTO(Long id, Long number, Date invoiceDate, Long senderId) {
        this.id = id;
        this.number = number;
        this.invoiceDate = invoiceDate;
        this.senderId = senderId;
    }

    public InvoiceDTO(Long number, Date invoiceDate, Long senderId) {
        this.id = null;
        this.number = number;
        this.invoiceDate = invoiceDate;
        this.senderId = senderId;
    }
}

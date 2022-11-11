package org.example.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

@Data
public final class Invoice implements Serializable {
    private final Long id;
    private final Long number;
    private final Date invoiceDate;
    private final Long senderId;
}

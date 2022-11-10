package org.example.entity;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public final class Invoice implements Serializable {
    private final Long id;
    private final Long number;
    private final Timestamp invoiceData;
    private final Long senderId;
}

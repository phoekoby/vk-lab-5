package org.example.mapper;

import generated.tables.Invoice;
import generated.tables.records.InvoiceRecord;

public interface Mapper<R, E> {
    R toRecord(E e);

    E toEntity(R r);
}

package org.example.mapper;

import generated.tables.records.InvoiceRecord;
import org.example.entity.Invoice;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;

public class InvoiceMapper implements Mapper<InvoiceRecord, Invoice> {
    @Override
    public InvoiceRecord toRecord(Invoice invoice) {
        final InvoiceRecord invoiceRecord = new InvoiceRecord();
        invoiceRecord.setId(invoice.getId());
        invoiceRecord.setNumber(invoice.getNumber());
        invoiceRecord.setInvoiceDate(invoice.getInvoiceDate().toLocalDate());
        invoiceRecord.setSenderId(invoice.getSenderId());
        return invoiceRecord;
    }

    @Override
    public Invoice toEntity(InvoiceRecord invoiceRecord) {
        return new Invoice(invoiceRecord.getId(), invoiceRecord.getNumber(), Date.valueOf(invoiceRecord.getInvoiceDate()), invoiceRecord.getSenderId());
    }
}

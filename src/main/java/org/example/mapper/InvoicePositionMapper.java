package org.example.mapper;


import generated.tables.records.InvoicePositionRecord;
import org.example.entity.InvoicePosition;

public class InvoicePositionMapper implements Mapper<InvoicePositionRecord, InvoicePosition> {
    @Override
    public InvoicePositionRecord toRecord(InvoicePosition invoicePosition) {
        final InvoicePositionRecord invoicePositionRecord = new InvoicePositionRecord();
        invoicePositionRecord.setId(invoicePosition.getId());
        invoicePositionRecord.setInvoiceId(invoicePosition.getInvoiceId());
        invoicePositionRecord.setAmount(invoicePosition.getAmount());
        invoicePositionRecord.setPrice(invoicePosition.getPrice());
        invoicePositionRecord.setProductId(invoicePosition.getProductId());
        return invoicePositionRecord;
    }

    @Override
    public InvoicePosition toEntity(InvoicePositionRecord invoicePositionRecord) {
        return new InvoicePosition(
                invoicePositionRecord.getId(),
                invoicePositionRecord.getPrice(),
                invoicePositionRecord.getAmount(),
                invoicePositionRecord.getProductId(),
                invoicePositionRecord.getInvoiceId());
    }
}

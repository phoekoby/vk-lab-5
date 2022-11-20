package org.example.mapper;


import generated.tables.records.InvoicePositionRecord;
import org.example.entity.InvoicePositionDTO;

public class InvoicePositionMapper implements Mapper<InvoicePositionRecord, InvoicePositionDTO> {
    @Override
    public InvoicePositionRecord toRecord(InvoicePositionDTO invoicePositionDTO) {
        final InvoicePositionRecord invoicePositionRecord = new InvoicePositionRecord();
        invoicePositionRecord.setId(invoicePositionDTO.getId());
        invoicePositionRecord.setInvoiceId(invoicePositionDTO.getInvoiceId());
        invoicePositionRecord.setAmount(invoicePositionDTO.getAmount());
        invoicePositionRecord.setPrice(invoicePositionDTO.getPrice());
        invoicePositionRecord.setProductId(invoicePositionDTO.getProductId());
        return invoicePositionRecord;
    }

    @Override
    public InvoicePositionDTO toEntity(InvoicePositionRecord invoicePositionRecord) {
        return new InvoicePositionDTO(
                invoicePositionRecord.getId(),
                invoicePositionRecord.getPrice(),
                invoicePositionRecord.getAmount(),
                invoicePositionRecord.getProductId(),
                invoicePositionRecord.getInvoiceId());
    }
}

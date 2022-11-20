package org.example.mapper;

import generated.tables.records.InvoiceRecord;
import org.example.entity.InvoiceDTO;

import java.sql.Date;

public class InvoiceMapper implements Mapper<InvoiceRecord, InvoiceDTO> {
    @Override
    public InvoiceRecord toRecord(InvoiceDTO invoiceDTO) {
        final InvoiceRecord invoiceRecord = new InvoiceRecord();
        invoiceRecord.setId(invoiceDTO.getId());
        invoiceRecord.setNumber(invoiceDTO.getNumber());
        invoiceRecord.setInvoiceDate(invoiceDTO.getInvoiceDate().toLocalDate());
        invoiceRecord.setSenderId(invoiceDTO.getSenderId());
        return invoiceRecord;
    }

    @Override
    public InvoiceDTO toEntity(InvoiceRecord invoiceRecord) {
        return new InvoiceDTO(invoiceRecord.getId(), invoiceRecord.getNumber(), Date.valueOf(invoiceRecord.getInvoiceDate()), invoiceRecord.getSenderId());
    }
}

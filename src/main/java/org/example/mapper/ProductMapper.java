package org.example.mapper;

import generated.tables.records.ProductRecord;
import org.example.entity.ProductDTO;

public class ProductMapper implements Mapper<ProductRecord, ProductDTO> {
    @Override
    public ProductRecord toRecord(ProductDTO productDTO) {
        final ProductRecord productRecord = new ProductRecord();
        productRecord.setId(productDTO.getId());
        productRecord.setName(productDTO.getName());
        productRecord.setInternalCode(productDTO.getInternalCode());
        return productRecord;
    }

    @Override
    public ProductDTO toEntity(ProductRecord productRecord) {
        return new ProductDTO(productRecord.getId(), productRecord.getName(), productRecord.getInternalCode());
    }
}

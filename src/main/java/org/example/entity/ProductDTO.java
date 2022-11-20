package org.example.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public final class ProductDTO implements Serializable {
    private final Long id;
    private final String name;
    private final Long internalCode;

    public ProductDTO(Long id, String name, Long internalCode) {
        this.id = id;
        this.name = name;
        this.internalCode = internalCode;
    }

    public ProductDTO(String name, Long internalCode) {
        this.id = null;
        this.name = name;
        this.internalCode = internalCode;
    }
}

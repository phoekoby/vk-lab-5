package org.example.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public final class Product implements Serializable {
    private final Long id;
    private final String name;
    private final Long internalCode;

    public Product(Long id, String name, Long internalCode) {
        this.id = id;
        this.name = name;
        this.internalCode = internalCode;
    }

    public Product(String name, Long internalCode) {
        this.id = null;
        this.name = name;
        this.internalCode = internalCode;
    }
}

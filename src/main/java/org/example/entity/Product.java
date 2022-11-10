package org.example.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public final class Product implements Serializable {
    private final Long id;
    private final String name;
    private final Long internalCode;
}

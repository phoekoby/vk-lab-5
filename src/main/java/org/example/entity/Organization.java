package org.example.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public final class Organization implements Serializable {
    private final Long id;
    private final Long INN;
    private final Long checkingAccount;
}

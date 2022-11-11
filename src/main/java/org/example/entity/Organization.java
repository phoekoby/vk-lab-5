package org.example.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public final class Organization implements Serializable {
    private final Long id;
    private final Long INN;
    private final Long checkingAccount;

    public Organization(Long INN, Long checkingAccount) {
        this.id = null;
        this.INN = INN;
        this.checkingAccount = checkingAccount;
    }

    public Organization(Long id, Long INN, Long checkingAccount) {
        this.id = id;
        this.INN = INN;
        this.checkingAccount = checkingAccount;
    }
}

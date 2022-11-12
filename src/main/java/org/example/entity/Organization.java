package org.example.entity;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

@Data
public final class Organization implements Serializable, Comparable<Organization> {
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


    @Override
    public int compareTo(@NotNull Organization o) {
        if (this.id == null && o.id == null) {
            return 0;
        } else if (this.id == null) {
            return -1;
        } else if (o.id == null) {
            return 1;
        }
        if (this.id - o.id > 0) {
            return 1;
        } else if (this.id - o.id < 0) {
            return -1;
        } else return 0;
    }
}

package org.example.config;

import org.jetbrains.annotations.NotNull;

public class DBCredentials {
    private final @NotNull String CONNECTION;

    private final @NotNull String DB_NAME;

    private final @NotNull String USERNAME;

    private final @NotNull String PASSWORD;

    public DBCredentials(@NotNull String CONNECTION, @NotNull String DB_NAME, @NotNull String USERNAME, @NotNull String PASSWORD) {
        this.CONNECTION = CONNECTION;
        this.DB_NAME = DB_NAME;
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;
    }

    public @NotNull String getCONNECTION() {
        return CONNECTION;
    }

    public @NotNull String getDB_NAME() {
        return DB_NAME;
    }

    public @NotNull String getUSERNAME() {
        return USERNAME;
    }

    public @NotNull String getPASSWORD() {
        return PASSWORD;
    }
}

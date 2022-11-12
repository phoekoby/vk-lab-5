package org.example.service;

import org.flywaydb.core.Flyway;

import static org.example.config.DbCredentials.*;

public final class MigrationService {
    private MigrationService() {
    }

    public static void createMigrations() {
        final var flyway = Flyway
                .configure()
                .dataSource(CONNECTION + DB_NAME, USERNAME, PASSWORD)
                .locations("db")
                .load();
        flyway.migrate();
    }
}

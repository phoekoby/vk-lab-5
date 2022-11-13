package org.example.service;

import com.google.inject.Inject;
import org.example.config.DBCredentials;
import org.flywaydb.core.Flyway;

public final class MigrationService {

    private final DBCredentials dbCredentials;

    @Inject
    public MigrationService(DBCredentials dbCredentials) {
        this.dbCredentials = dbCredentials;
    }

    public  void createMigrations() {
        final var flyway = Flyway
                .configure()
                .dataSource(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                        dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())
                .locations("db")
                .load();
        flyway.migrate();
    }
}

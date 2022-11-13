package config;

import com.google.inject.Inject;
import org.example.config.DBCredentials;
import org.flywaydb.core.Flyway;

import static org.example.config.DbConstants.*;
import static org.example.config.DbConstants.PASSWORD;

public class TestMigrationService {
    private final DBCredentials dbCredentials;

    @Inject
    public TestMigrationService(DBCredentials dbCredentials) {
        this.dbCredentials = dbCredentials;
    }
    public void initializeDb() {
        final var flyway = Flyway
                .configure()
                .dataSource(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                        dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())
                .locations("db")
                .load();
        flyway.migrate();
    }

    public void cleanDb(){
        final var flyway = Flyway
                .configure()
                .dataSource(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                        dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())
                .cleanDisabled(false)
                .locations("db")
                .load();
        flyway.clean();
    }
}

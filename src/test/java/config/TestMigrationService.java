package config;

import com.google.inject.Inject;
import org.example.config.DBCredentials;
import org.flywaydb.core.Flyway;


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
                .locations("test-migrations")
                .load();
        flyway.migrate();
    }

    public void cleanDb(){
        final var flyway = Flyway
                .configure()
                .dataSource(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                        dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())
                .cleanDisabled(false)
                .locations("test-migrations")
                .load();
        flyway.clean();
    }
}

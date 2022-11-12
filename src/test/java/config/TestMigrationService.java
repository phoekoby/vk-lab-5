package config;

import org.flywaydb.core.Flyway;

import static org.example.config.DbCredentials.*;
import static org.example.config.DbCredentials.PASSWORD;

public class TestMigrationService {
    private TestMigrationService() {
    }

    public static void initializeDb() {
        final var flyway = Flyway
                .configure()
                .dataSource(CONNECTION + DB_NAME, USERNAME, PASSWORD)
                .locations("db")
                .load();
        flyway.migrate();
    }

    public static void cleanDb(){
        final var flyway = Flyway
                .configure()
                .dataSource(CONNECTION + DB_NAME, USERNAME, PASSWORD)
                .cleanDisabled(false)
                .locations("db")
                .load();
        flyway.clean();
    }
}

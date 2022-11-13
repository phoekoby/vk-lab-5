import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.opentable.db.postgres.embedded.DatabasePreparer;
import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import com.opentable.db.postgres.embedded.FlywayPreparer;
import config.TestMigrationService;
import config.TestModule;
import org.example.dao.InvoiceDAO;
import org.example.dao.InvoicePositionDAO;
import org.example.dao.OrganizationDAO;
import org.example.dao.ProductDAO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import javax.sql.DataSource;
import java.io.IOException;

public abstract class AbstractDataBaseTest {
    @Inject
    protected InvoiceDAO invoiceDAO;
    @Inject
    protected OrganizationDAO organizationDAO;
    @Inject
    protected InvoicePositionDAO invoicePositionDAO;
    @Inject
    protected ProductDAO productDAO;

    @Inject
    private TestMigrationService migrationService;

    @BeforeEach
    public void beforeEach() {
        Injector injector = Guice.createInjector(new TestModule());
        injector.injectMembers(this);
        migrationService.initializeDb();
    }

    @AfterEach
    public void afterEach() {
        migrationService.cleanDb();
    }
}

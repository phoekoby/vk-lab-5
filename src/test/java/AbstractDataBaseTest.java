import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import config.TestMigrationService;
import config.TestModule;
import org.example.dao.InvoiceDAO;
import org.example.dao.InvoicePositionDAO;
import org.example.dao.OrganizationDAO;
import org.example.dao.ProductDAO;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractDataBaseTest {
    @Inject
    protected InvoiceDAO invoiceDAO;
    @Inject
    protected OrganizationDAO organizationDAO;
    @Inject
    protected InvoicePositionDAO invoicePositionDAO;
    @Inject
    protected ProductDAO productDAO;

    @BeforeEach
    public void beforeEach() {
        TestMigrationService.initializeDb();
        Injector injector = Guice.createInjector(new TestModule());
        injector.injectMembers(this);
    }

    @AfterEach
    public void afterEach() {
        TestMigrationService.cleanDb();
    }
}

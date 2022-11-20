package config;

import com.google.inject.AbstractModule;
import org.example.config.DBCredentials;
import org.example.dao.InvoiceDAO;
import org.example.dao.InvoicePositionDAO;
import org.example.dao.OrganizationDAO;
import org.example.dao.ProductDAO;
import org.example.dao.impl.InvoiceJooqDAOImpl;
import org.example.dao.impl.InvoicePositionJooqDAOImpl;
import org.example.dao.impl.OrganizationJooqDAOImpl;
import org.example.dao.impl.ProductJooqDAOImpl;
import org.example.service.ManagementService;

import static config.TestDbConstants.*;


public class TestModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(TestMigrationService.class);
        bind(InvoiceDAO.class).to(InvoiceJooqDAOImpl.class);
        bind(InvoicePositionDAO.class).to(InvoicePositionJooqDAOImpl.class);
        bind(OrganizationDAO.class).to(OrganizationJooqDAOImpl.class);
        bind(ProductDAO.class).to(ProductJooqDAOImpl.class);
        bind(ManagementService.class);
        bind(DBCredentials.class).toInstance(new DBCredentials(CONNECTION, DB_NAME, USERNAME, PASSWORD));
    }
}

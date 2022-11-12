package config;

import com.google.inject.AbstractModule;
import org.example.dao.InvoiceDAO;
import org.example.dao.InvoicePositionDAO;
import org.example.dao.OrganizationDAO;
import org.example.dao.ProductDAO;
import org.example.dao.impl.InvoiceDAOImpl;
import org.example.dao.impl.InvoicePositionDAOImpl;
import org.example.dao.impl.OrganizationDAOImpl;
import org.example.dao.impl.ProductDAOImpl;
import org.example.service.ManagementService;

public class TestModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(InvoiceDAO.class).to(InvoiceDAOImpl.class);
        bind(InvoicePositionDAO.class).to(InvoicePositionDAOImpl.class);
        bind(OrganizationDAO.class).to(OrganizationDAOImpl.class);
        bind(ProductDAO.class).to(ProductDAOImpl.class);
        bind(ManagementService.class);
    }
}

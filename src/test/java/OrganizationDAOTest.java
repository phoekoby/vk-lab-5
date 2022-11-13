import org.example.entity.Invoice;
import org.example.entity.Organization;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrganizationDAOTest extends AbstractDataBaseTest {
    @Test
    public void getAll() {
        Collection<Organization> organizations = organizationDAO.getAll();
        assertTrue(organizations.containsAll(
                List.of(
                       new Organization(1L, 234532545L, 6234522435L),
                       new Organization(2L, 854923945L,  23485828L),
                       new Organization(3L, 76830242L, 4859234689L),
                       new Organization(4L, 621113248L, 12348125L)
                )));
        assertEquals(organizations.size(), 4);
    }

    @Test
    public void getById() {
        Long existingInvoiceId = 1L;
        Long notExistingInvoiceId = 7L;
        Optional<Invoice> invoice1 = invoiceDAO.getById(existingInvoiceId);
        assertTrue(invoice1.isPresent());
        assertEquals(invoice1.get(), new Invoice(1L, 1L, Date.valueOf("2022-11-09"), 1L));
        Optional<Invoice> invoice2 = invoiceDAO.getById(notExistingInvoiceId);
        assertTrue(invoice2.isEmpty());
    }

    @Test
    public void delete() {
        Long existingInvoiceId = 1L;
        Optional<Invoice> invoice1 = invoiceDAO.getById(existingInvoiceId);
        assertTrue(invoice1.isPresent());
        invoiceDAO.delete(existingInvoiceId);
        invoice1 = invoiceDAO.getById(existingInvoiceId);
        assertTrue(invoice1.isEmpty());
    }

    @Test
    public void update() {
        Invoice existingInvoice = new Invoice(1L, 1L, Date.valueOf("2022-11-09"), 1L);
        Invoice existingChangedInvoice = new Invoice(1L, 1L, Date.valueOf("2022-11-11"), 2L);
        Invoice notExistingInvoice = new Invoice(7L, 1L, Date.valueOf("2022-11-09"), 1L);

        Optional<Invoice> invoice1 = invoiceDAO.getById(existingInvoice.getId());
        assertTrue(invoice1.isPresent());
        assertEquals(existingInvoice, invoice1.get());
        invoiceDAO.update(existingChangedInvoice);
        invoice1 = invoiceDAO.getById(existingInvoice.getId());
        assertTrue(invoice1.isPresent());
        assertEquals(invoice1.get(), existingChangedInvoice);

        assertThrows(IllegalArgumentException.class, () -> invoiceDAO.update(notExistingInvoice));
    }

    @Test
    public void save() {
        Invoice notExistingInvoice = new Invoice(7L, 1L, Date.valueOf("2022-11-09"), 1L);
        Optional<Invoice> invoice = invoiceDAO.getById(7L);
        assertTrue(invoice.isEmpty());

        invoiceDAO.save(notExistingInvoice);
        invoice = invoiceDAO.getById(7L);
        assertTrue(invoice.isPresent());
        assertEquals(invoice.get(), notExistingInvoice);
    }

    @Test
    public void saveAll() {
        int sizeDbShouldBeAfterSaving = 8;
        Invoice notExistingInvoice1 = new Invoice(7L, 1L, Date.valueOf("2022-11-09"), 1L);
        Invoice notExistingInvoice2 = new Invoice(8L, 1L, Date.valueOf("2022-11-09"), 1L);
        Collection<Invoice> notExistingInvoices = List.of(notExistingInvoice2, notExistingInvoice1);
        Optional<Invoice> invoice1 = invoiceDAO.getById(7L);
        Optional<Invoice> invoice2 = invoiceDAO.getById(8L);
        assertTrue(invoice1.isEmpty());
        assertTrue(invoice2.isEmpty());

        Collection<Invoice> result = invoiceDAO.saveAll(notExistingInvoices);
        for (Invoice i : notExistingInvoices) {
            Optional<Invoice> invoice = invoiceDAO.getById(i.getId());
            assertTrue(invoice.isPresent());
            assertTrue(notExistingInvoices.contains(invoice.get()));
            assertTrue(result.contains(invoice.get()));
        }
        assertEquals(result.size(), notExistingInvoices.size());
        assertEquals(sizeDbShouldBeAfterSaving, invoiceDAO.getAll().size());
    }

}

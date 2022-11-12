import org.example.entity.Invoice;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class InvoiceDAOTest extends AbstractDataBaseTest {

    @Test
    public void getAllInvoice() {
        Collection<Invoice> invoices = invoiceDAO.getAll();
        assertTrue(invoices.containsAll(
                List.of(new Invoice(1L, 1L, Date.valueOf("2022-11-09"), 1L),
                        new Invoice(2L, 2L, Date.valueOf("2022-11-10"), 1L),
                        new Invoice(3L, 3L, Date.valueOf("2022-11-11"), 2L),
                        new Invoice(4L, 4L, Date.valueOf("2022-11-12"), 3L),
                        new Invoice(5L, 5L, Date.valueOf("2022-11-13"), 3L),
                        new Invoice(6L, 6L, Date.valueOf("2022-11-14"), 2L)
                )));
        assertEquals(invoices.size(), 6);
    }

    @Test
    public void getByIdInvoice() {
        Long existingInvoiceId = 1L;
        Long notExistingInvoiceId = 7L;
        Optional<Invoice> invoice1 = invoiceDAO.getById(existingInvoiceId);
        assertTrue(invoice1.isPresent());
        assertEquals(invoice1.get(), new Invoice(1L, 1L, Date.valueOf("2022-11-09"), 1L));
        Optional<Invoice> invoice2 = invoiceDAO.getById(notExistingInvoiceId);
        assertTrue(invoice2.isEmpty());
    }

    @Test
    public void deleteInvoice() {
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
    public void saveInvoice() {
        Invoice notExistingInvoice = new Invoice(7L, 1L, Date.valueOf("2022-11-09"), 1L);
        Optional<Invoice> invoice = invoiceDAO.getById(7L);
        assertTrue(invoice.isEmpty());

        invoiceDAO.save(notExistingInvoice);
        invoice = invoiceDAO.getById(7L);
        assertTrue(invoice.isPresent());
        assertEquals(invoice.get(), notExistingInvoice);
    }

    @Test
    public void saveAllInvoices() {
        int sizeDbShouldBeAfterSaving = 8;
        Invoice notExistingInvoice1 = new Invoice(7L, 1L, Date.valueOf("2022-11-09"), 1L);
        Invoice notExistingInvoice2 = new Invoice(8L, 1L, Date.valueOf("2022-11-09"), 1L);
        Collection<Invoice> notExistingInvoices = List.of(notExistingInvoice2, notExistingInvoice1);
        Optional<Invoice> invoice1 = invoiceDAO.getById(7L);
        Optional<Invoice> invoice2 = invoiceDAO.getById(8L);
        assertTrue(invoice1.isEmpty());
        assertTrue(invoice2.isEmpty());

        Collection<Invoice> result = invoiceDAO.saveAll(notExistingInvoices);
        for (Invoice i: notExistingInvoices) {
            Optional<Invoice> invoice = invoiceDAO.getById(i.getId());
            assertTrue(invoice.isPresent());
            assertTrue(notExistingInvoices.contains(invoice.get()));
            assertTrue(result.contains(invoice.get()));
        }
        assertEquals(result.size(), notExistingInvoices.size());
        assertEquals(sizeDbShouldBeAfterSaving, invoiceDAO.getAll().size());
    }

}

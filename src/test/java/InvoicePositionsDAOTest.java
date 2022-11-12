import org.example.entity.Invoice;
import org.example.entity.InvoicePosition;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InvoicePositionsDAOTest extends AbstractDataBaseTest{


    @Test
    public void getAll() {
        Collection<InvoicePosition> invoices = invoicePositionDAO.getAll();
        assertTrue(invoices.containsAll(
                List.of(
                        new InvoicePosition(1L, 100.0, 1000, 1L, 1L),
                        new InvoicePosition(2L, 1200.0, 10, 2L, 1L),
                        new InvoicePosition(3L, 10.0, 100000, 3L, 1L),
                        new InvoicePosition(4L, 100.0, 10, 1L, 2L),
                        new InvoicePosition(5L, 10.0, 1000, 1L, 3L),
                        new InvoicePosition(6L, 100., 1000, 1L, 1L),
                        new InvoicePosition(7L, 100.0, 1000, 1L, 1L),
                        new InvoicePosition(8L, 100.0, 10000, 1L, 4L),
                        new InvoicePosition(9L, 100.0, 10000, 1L, 5L),
                        new InvoicePosition(10L, 100.0, 1000, 1L, 6L),
                        new InvoicePosition(11L, 100.0, 1000, 1L, 1L),
                        new InvoicePosition(12L, 100.0, 1000, 1L, 1L)
                )));
        assertEquals(invoices.size(), 12);
    }

    @Test
    public void getById() {
        Long existingInvoicePositionId = 1L;
        Long notExistingInvoicePositionId = 13L;
        Optional<InvoicePosition> invoicePosition1 = invoicePositionDAO.getById(existingInvoicePositionId);
        assertTrue(invoicePosition1.isPresent());
        assertEquals(invoicePosition1.get(),  new InvoicePosition(1L, 100.0, 1000, 1L, 1L));
        Optional<InvoicePosition> invoicePosition2 = invoicePositionDAO.getById(notExistingInvoicePositionId);
        assertTrue(invoicePosition2.isEmpty());
    }

    @Test
    public void delete() {
        Long existingInvoicePositionId = 1L;
        Optional<InvoicePosition> invoicePosition1 = invoicePositionDAO.getById(existingInvoicePositionId);
        assertTrue(invoicePosition1.isPresent());
        invoicePositionDAO.delete(existingInvoicePositionId);
        invoicePosition1 = invoicePositionDAO.getById(existingInvoicePositionId);
        assertTrue(invoicePosition1.isEmpty());
    }
//TODO: доделать
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
    public void saveInvoicePositions() {
        Invoice notExistingInvoice = new Invoice(7L, 1L, Date.valueOf("2022-11-09"), 1L);
        Optional<Invoice> invoice = invoiceDAO.getById(7L);
        assertTrue(invoice.isEmpty());

        invoiceDAO.save(notExistingInvoice);
        invoice = invoiceDAO.getById(7L);
        assertTrue(invoice.isPresent());
        assertEquals(invoice.get(), notExistingInvoice);
    }

    @Test
    public void saveAllInvoicesPositions() {
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

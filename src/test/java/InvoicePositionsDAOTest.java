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

    @Test
    public void update() {
        InvoicePosition existingInvoicePosition =  new InvoicePosition(1L, 100.0, 1000, 1L, 1L);
        InvoicePosition existingChangedInvoicePosition =  new InvoicePosition(1L, 120.0, 1100, 1L, 1L);
        InvoicePosition notExistingInvoice = new InvoicePosition(13L, 120.0, 1100, 1L, 1L);

        Optional<InvoicePosition> invoice1 = invoicePositionDAO.getById(existingInvoicePosition.getId());
        assertTrue(invoice1.isPresent());
        assertEquals(existingInvoicePosition, invoice1.get());
        invoicePositionDAO.update(existingChangedInvoicePosition);
        invoice1 = invoicePositionDAO.getById(existingInvoicePosition.getId());
        assertTrue(invoice1.isPresent());
        assertEquals(invoice1.get(), existingChangedInvoicePosition);

        assertThrows(IllegalArgumentException.class, () -> invoicePositionDAO.update(notExistingInvoice));
    }

    @Test
    public void saveInvoicePositions() {
        InvoicePosition notExistingInvoicePosition = new InvoicePosition(13L, 120.0, 1100, 1L, 1L);
        Optional<InvoicePosition> invoice = invoicePositionDAO.getById(13L);
        assertTrue(invoice.isEmpty());

        invoicePositionDAO.save(notExistingInvoicePosition);
        invoice = invoicePositionDAO.getById(13L);
        assertTrue(invoice.isPresent());
        assertEquals(invoice.get(), notExistingInvoicePosition);
    }

    @Test
    public void saveAllInvoicesPositions() {
        int sizeDbShouldBeAfterSaving = 14;
        InvoicePosition notExistingInvoicePosition1 =new InvoicePosition(13L, 120.0, 1100, 1L, 1L);
        InvoicePosition notExistingInvoicePosition2 = new InvoicePosition(14L, 120.0, 1100, 1L, 1L);
        Collection<InvoicePosition> notExistingInvoicePositions = List.of(notExistingInvoicePosition2, notExistingInvoicePosition1);
        Optional<InvoicePosition> invoice1 = invoicePositionDAO.getById(13L);
        Optional<InvoicePosition> invoice2 = invoicePositionDAO.getById(14L);
        assertTrue(invoice1.isEmpty());
        assertTrue(invoice2.isEmpty());

        Collection<InvoicePosition> result = invoicePositionDAO.saveAll(notExistingInvoicePositions);
        for (InvoicePosition i: notExistingInvoicePositions) {
            Optional<InvoicePosition> invoice = invoicePositionDAO.getById(i.getId());
            assertTrue(invoice.isPresent());
            assertTrue(notExistingInvoicePositions.contains(invoice.get()));
            assertTrue(result.contains(invoice.get()));
        }
        assertEquals(result.size(), notExistingInvoicePositions.size());
        assertEquals(sizeDbShouldBeAfterSaving, invoicePositionDAO.getAll().size());
    }

}

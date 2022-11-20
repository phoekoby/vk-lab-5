import org.example.entity.InvoiceDTO;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class InvoiceDTODAOTest extends AbstractDataBaseTest {

    @Test
    public void getAllInvoice() {
        Collection<InvoiceDTO> invoiceDTOS = invoiceDAO.getAll();
        assertTrue(invoiceDTOS.containsAll(
                List.of(new InvoiceDTO(1L, 1L, Date.valueOf("2022-11-09"), 1L),
                        new InvoiceDTO(2L, 2L, Date.valueOf("2022-11-10"), 1L),
                        new InvoiceDTO(3L, 3L, Date.valueOf("2022-11-11"), 2L),
                        new InvoiceDTO(4L, 4L, Date.valueOf("2022-11-12"), 3L),
                        new InvoiceDTO(5L, 5L, Date.valueOf("2022-11-13"), 3L),
                        new InvoiceDTO(6L, 6L, Date.valueOf("2022-11-14"), 2L)
                )));
        assertEquals(invoiceDTOS.size(), 6);
    }

    @Test
    public void getByIdInvoice() {
        Long existingInvoiceId = 1L;
        Long notExistingInvoiceId = 7L;
        Optional<InvoiceDTO> invoice1 = invoiceDAO.getById(existingInvoiceId);
        assertTrue(invoice1.isPresent());
        assertEquals(invoice1.get(), new InvoiceDTO(1L, 1L, Date.valueOf("2022-11-09"), 1L));
        Optional<InvoiceDTO> invoice2 = invoiceDAO.getById(notExistingInvoiceId);
        assertTrue(invoice2.isEmpty());
    }

    @Test
    public void deleteInvoice() {
        Long existingInvoiceId = 1L;
        Optional<InvoiceDTO> invoice1 = invoiceDAO.getById(existingInvoiceId);
        assertTrue(invoice1.isPresent());
        invoiceDAO.delete(existingInvoiceId);
        invoice1 = invoiceDAO.getById(existingInvoiceId);
        assertTrue(invoice1.isEmpty());
    }

    @Test
    public void update() {
        InvoiceDTO existingInvoiceDTO = new InvoiceDTO(1L, 1L, Date.valueOf("2022-11-09"), 1L);
        InvoiceDTO existingChangedInvoiceDTO = new InvoiceDTO(1L, 1L, Date.valueOf("2022-11-11"), 2L);
        InvoiceDTO notExistingInvoiceDTO = new InvoiceDTO(7L, 1L, Date.valueOf("2022-11-09"), 1L);

        Optional<InvoiceDTO> invoice1 = invoiceDAO.getById(existingInvoiceDTO.getId());
        assertTrue(invoice1.isPresent());
        assertEquals(existingInvoiceDTO, invoice1.get());
        invoiceDAO.update(existingChangedInvoiceDTO);
        invoice1 = invoiceDAO.getById(existingInvoiceDTO.getId());
        assertTrue(invoice1.isPresent());
        assertEquals(invoice1.get(), existingChangedInvoiceDTO);

        assertThrows(IllegalArgumentException.class, () -> invoiceDAO.update(notExistingInvoiceDTO));
    }

    @Test
    public void saveInvoice() {
        InvoiceDTO notExistingInvoiceDTO = new InvoiceDTO(1L, Date.valueOf("2022-11-09"), 1L);
        Optional<InvoiceDTO> invoice = invoiceDAO.getById(7L);
        assertTrue(invoice.isEmpty());

        invoiceDAO.save(notExistingInvoiceDTO);
        invoice = invoiceDAO.getById(7L);
        assertTrue(invoice.isPresent());
    }

    @Test
    public void saveAllInvoices() {
        int sizeDbShouldBeAfterSaving = 8;
        InvoiceDTO notExistingInvoice1DTO = new InvoiceDTO(1L, Date.valueOf("2022-11-09"), 1L);
        InvoiceDTO notExistingInvoice2DTO = new InvoiceDTO(1L, Date.valueOf("2022-11-09"), 1L);
        Collection<InvoiceDTO> notExistingInvoiceDTOS = List.of(notExistingInvoice2DTO, notExistingInvoice1DTO);

        Collection<InvoiceDTO> result = invoiceDAO.saveAll(notExistingInvoiceDTOS);
        assertEquals(result.size(), 2);
        assertEquals(sizeDbShouldBeAfterSaving, invoiceDAO.getAll().size());
    }

}

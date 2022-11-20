import org.example.entity.InvoicePositionDTO;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class InvoiceDTOPositionsDAOTest extends AbstractDataBaseTest{


    @Test
    public void getAll() {
        Collection<InvoicePositionDTO> invoices = invoicePositionDAO.getAll();
        assertTrue(invoices.containsAll(
                List.of(
                        new InvoicePositionDTO(1L, 100.0, 1000, 1L, 1L),
                        new InvoicePositionDTO(2L, 1200.0, 10, 2L, 1L),
                        new InvoicePositionDTO(3L, 10.0, 100000, 3L, 1L),
                        new InvoicePositionDTO(4L, 100.0, 10, 1L, 2L),
                        new InvoicePositionDTO(5L, 10.0, 1000, 1L, 3L),
                        new InvoicePositionDTO(6L, 100., 1000, 1L, 1L),
                        new InvoicePositionDTO(7L, 100.0, 1000, 1L, 4L),
                        new InvoicePositionDTO(8L, 125.0, 10000, 1L, 4L),
                        new InvoicePositionDTO(9L, 100.0, 10000, 1L, 5L),
                        new InvoicePositionDTO(10L, 100.0, 1000, 2L, 6L),
                        new InvoicePositionDTO(11L, 100.0, 1000, 1L, 1L),
                        new InvoicePositionDTO(12L, 100.0, 1000, 1L, 1L)
                )));
        assertEquals(invoices.size(), 12);
    }

    @Test
    public void getById() {
        Long existingInvoicePositionId = 1L;
        Long notExistingInvoicePositionId = 13L;
        Optional<InvoicePositionDTO> invoicePosition1 = invoicePositionDAO.getById(existingInvoicePositionId);
        assertTrue(invoicePosition1.isPresent());
        assertEquals(invoicePosition1.get(),  new InvoicePositionDTO(1L, 100.0, 1000, 1L, 1L));
        Optional<InvoicePositionDTO> invoicePosition2 = invoicePositionDAO.getById(notExistingInvoicePositionId);
        assertTrue(invoicePosition2.isEmpty());
    }

    @Test
    public void delete() {
        Long existingInvoicePositionId = 1L;
        Optional<InvoicePositionDTO> invoicePosition1 = invoicePositionDAO.getById(existingInvoicePositionId);
        assertTrue(invoicePosition1.isPresent());
        invoicePositionDAO.delete(existingInvoicePositionId);
        invoicePosition1 = invoicePositionDAO.getById(existingInvoicePositionId);
        assertTrue(invoicePosition1.isEmpty());
    }

    @Test
    public void update() {
        InvoicePositionDTO existingInvoicePositionDTO =  new InvoicePositionDTO(1L, 100.0, 1000, 1L, 1L);
        InvoicePositionDTO existingChangedInvoicePositionDTO =  new InvoicePositionDTO(1L, 120.0, 1100, 1L, 1L);
        InvoicePositionDTO notExistingInvoice = new InvoicePositionDTO(13L, 120.0, 1100, 1L, 1L);

        Optional<InvoicePositionDTO> invoice1 = invoicePositionDAO.getById(existingInvoicePositionDTO.getId());
        assertTrue(invoice1.isPresent());
        assertEquals(existingInvoicePositionDTO, invoice1.get());
        invoicePositionDAO.update(existingChangedInvoicePositionDTO);
        invoice1 = invoicePositionDAO.getById(existingInvoicePositionDTO.getId());
        assertTrue(invoice1.isPresent());
        assertEquals(invoice1.get(), existingChangedInvoicePositionDTO);

        assertThrows(IllegalArgumentException.class, () -> invoicePositionDAO.update(notExistingInvoice));
    }

    @Test
    public void saveInvoicePositions() {
        InvoicePositionDTO notExistingInvoicePositionDTO = new InvoicePositionDTO(120.0, 1100, 1L, 1L);
        Optional<InvoicePositionDTO> invoice = invoicePositionDAO.getById(13L);
        assertTrue(invoice.isEmpty());

        invoicePositionDAO.save(notExistingInvoicePositionDTO);
        invoice = invoicePositionDAO.getById(13L);
        assertTrue(invoice.isPresent());
    }

    @Test
    public void saveAllInvoicesPositions() {
        int sizeDbShouldBeAfterSaving = 14;
        InvoicePositionDTO notExistingInvoicePosition1DTO =new InvoicePositionDTO( 120.0, 1100, 1L, 1L);
        InvoicePositionDTO notExistingInvoicePosition2DTO = new InvoicePositionDTO(120.0, 1100, 1L, 1L);
        Collection<InvoicePositionDTO> notExistingInvoicePositionDTOS = List.of(notExistingInvoicePosition2DTO, notExistingInvoicePosition1DTO);

        Collection<InvoicePositionDTO> result = invoicePositionDAO.saveAll(notExistingInvoicePositionDTOS);
        assertEquals(result.size(), 2);
        assertEquals(sizeDbShouldBeAfterSaving, invoicePositionDAO.getAll().size());
    }

}

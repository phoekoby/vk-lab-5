import org.example.entity.OrganizationDTO;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class OrganizationDTODAOTest extends AbstractDataBaseTest {
    @Test
    public void getAll() {
        Collection<OrganizationDTO> organizationDTOS = organizationDAO.getAll();
        assertTrue(organizationDTOS.containsAll(
                List.of(
                       new OrganizationDTO(1L, 234532545L, 6234522435L),
                       new OrganizationDTO(2L, 854923945L,  23485828L),
                       new OrganizationDTO(3L, 76830242L, 4859234689L),
                       new OrganizationDTO(4L, 621113248L, 12348125L)
                )));
        assertEquals(organizationDTOS.size(), 4);
    }

    @Test
    public void getById() {
        Long existingOrganizationId = 1L;
        Long notExistingOrganizationId = 7L;
        Optional<OrganizationDTO> organization = organizationDAO.getById(existingOrganizationId);
        assertTrue(organization.isPresent());
        assertEquals(organization.get(),  new OrganizationDTO(1L, 234532545L, 6234522435L));
        Optional<OrganizationDTO> organization2 = organizationDAO.getById(notExistingOrganizationId);
        assertTrue(organization2.isEmpty());
    }

    @Test
    public void delete() {
        Long existingOrganizationId = 1L;
        Optional<OrganizationDTO> organization = organizationDAO.getById(existingOrganizationId);
        assertTrue(organization.isPresent());
        organizationDAO.delete(existingOrganizationId);
        organization = organizationDAO.getById(existingOrganizationId);
        assertTrue(organization.isEmpty());
    }

    @Test
    public void update() {
        OrganizationDTO existingOrganizationDTO = new OrganizationDTO(1L, 234532545L, 6234522435L);
        OrganizationDTO existingChangedOrganizationDTO = new OrganizationDTO(1L, 1L, 1L);
        OrganizationDTO notExisting= new OrganizationDTO(7L, 1L, 1L);

        Optional<OrganizationDTO> organization = organizationDAO.getById(existingOrganizationDTO.getId());
        assertTrue(organization.isPresent());
        assertEquals(existingOrganizationDTO, organization.get());
        organizationDAO.update(existingChangedOrganizationDTO);
        organization = organizationDAO.getById(existingOrganizationDTO.getId());
        assertTrue(organization.isPresent());
        assertEquals(organization.get(), existingChangedOrganizationDTO);

        assertThrows(IllegalArgumentException.class, () -> organizationDAO.update(notExisting));
    }

    @Test
    public void save() {
        OrganizationDTO notExistingOrganizationDTO =new OrganizationDTO( 232344532545L, 6234522342435L);
        Optional<OrganizationDTO> organization = organizationDAO.getById(5L);
        assertTrue(organization.isEmpty());

        organizationDAO.save(notExistingOrganizationDTO);
        organization = organizationDAO.getById(5L);
        assertTrue(organization.isPresent());
    }

    @Test
    public void saveAll() {
        int dbSizeBefore = organizationDAO.getAll().size();
        int sizeDbShouldBeAfterSaving = dbSizeBefore + 2;
        OrganizationDTO notExistingOrganization1DTO = new OrganizationDTO( 1L, 1L);
        OrganizationDTO notExistingOrganization2DTO = new OrganizationDTO( 2L, 2L);
        Collection<OrganizationDTO> notExistingOrganizationDTOS = List.of(notExistingOrganization1DTO, notExistingOrganization2DTO);
        Collection<OrganizationDTO> result = organizationDAO.saveAll(notExistingOrganizationDTOS);
        assertEquals(result.size(), 2);
        assertEquals(sizeDbShouldBeAfterSaving, organizationDAO.getAll().size());
    }

    @Test
    public void findFirst10OrganizationsByProduct(){
        OrganizationDTO o1 = organizationDAO.getById(1L).get();
        OrganizationDTO o2 = organizationDAO.getById(2L).get();
        OrganizationDTO o3 = organizationDAO.getById(3L).get();
        OrganizationDTO o4 = organizationDAO.getById(4L).get();
        List<OrganizationDTO> forProduct1 = List.of(o3, o1, o2, o4);
        List<OrganizationDTO> forProduct2 = List.of(o2, o1, o3, o4);
        List<OrganizationDTO> forProduct3 = List.of(o1, o2, o3, o4);
        List<OrganizationDTO> forProduct4 = List.of(o1, o2, o3, o4);


        assertEquals(forProduct1, organizationDAO.findFirst10OrganizationsByProduct(1L));
        assertEquals(forProduct2, organizationDAO.findFirst10OrganizationsByProduct(2L));
        assertEquals(forProduct3, organizationDAO.findFirst10OrganizationsByProduct(3L));
        assertEquals(forProduct4, organizationDAO.findFirst10OrganizationsByProduct(4L));
    }

    @Test
    public void findOrganizationAmountProductMoreThanValue(){
        Map<Long, Integer> testData1 = Map.of(3L, 500, 2L, 7);
        Map<Long, Integer> testData2 = Map.of(1L, 10000);
        Map<Long, Integer> testData3 = Map.of(1L, 1000000);
        Map<Long, Integer> testData4 = Map.of(1L, 1);

        OrganizationDTO o1 = organizationDAO.getById(1L).get();
        OrganizationDTO o2 = organizationDAO.getById(2L).get();
        OrganizationDTO o3 = organizationDAO.getById(3L).get();
        OrganizationDTO o4 = organizationDAO.getById(4L).get();

        assertTrue(organizationDAO.findOrganizationAmountProductMoreThanValue(testData1).contains(o1));
        assertTrue(organizationDAO.findOrganizationAmountProductMoreThanValue(testData2).contains(o3));
        assertTrue(organizationDAO.findOrganizationAmountProductMoreThanValue(testData3).isEmpty());
        assertTrue(organizationDAO.findOrganizationAmountProductMoreThanValue(testData4).containsAll(List.of(o1, o2,o3)));
    }

}

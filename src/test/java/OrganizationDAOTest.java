import org.example.entity.Organization;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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
        Long existingOrganizationId = 1L;
        Long notExistingOrganizationId = 7L;
        Optional<Organization> organization = organizationDAO.getById(existingOrganizationId);
        assertTrue(organization.isPresent());
        assertEquals(organization.get(),  new Organization(1L, 234532545L, 6234522435L));
        Optional<Organization> organization2 = organizationDAO.getById(notExistingOrganizationId);
        assertTrue(organization2.isEmpty());
    }

    @Test
    public void delete() {
        Long existingOrganizationId = 1L;
        Optional<Organization> organization = organizationDAO.getById(existingOrganizationId);
        assertTrue(organization.isPresent());
        organizationDAO.delete(existingOrganizationId);
        organization = organizationDAO.getById(existingOrganizationId);
        assertTrue(organization.isEmpty());
    }

    @Test
    public void update() {
        Organization existingOrganization = new Organization(1L, 234532545L, 6234522435L);
        Organization existingChangedOrganization = new Organization(1L, 1L, 1L);
        Organization notExisting= new Organization(7L, 1L, 1L);

        Optional<Organization> organization = organizationDAO.getById(existingOrganization.getId());
        assertTrue(organization.isPresent());
        assertEquals(existingOrganization, organization.get());
        organizationDAO.update(existingChangedOrganization);
        organization = organizationDAO.getById(existingOrganization.getId());
        assertTrue(organization.isPresent());
        assertEquals(organization.get(), existingChangedOrganization);

        assertThrows(IllegalArgumentException.class, () -> organizationDAO.update(notExisting));
    }

    @Test
    public void save() {
        Organization notExistingOrganization =new Organization( 232344532545L, 6234522342435L);
        Optional<Organization> organization = organizationDAO.getById(5L);
        assertTrue(organization.isEmpty());

        organizationDAO.save(notExistingOrganization);
        organization = organizationDAO.getById(5L);
        assertTrue(organization.isPresent());
    }

    @Test
    public void saveAll() {
        int dbSizeBefore = organizationDAO.getAll().size();
        int sizeDbShouldBeAfterSaving = dbSizeBefore + 2;
        Organization notExistingOrganization1 = new Organization( 1L, 1L);
        Organization notExistingOrganization2 = new Organization( 2L, 2L);
        Collection<Organization> notExistingOrganizations = List.of(notExistingOrganization1, notExistingOrganization2);
        Collection<Organization> result = organizationDAO.saveAll(notExistingOrganizations);
        assertEquals(result.size(), 2);
        assertEquals(sizeDbShouldBeAfterSaving, organizationDAO.getAll().size());
    }

    @Test
    public void findFirst10OrganizationsByProduct(){
        Organization o1 = organizationDAO.getById(1L).get();
        Organization o2 = organizationDAO.getById(2L).get();
        Organization o3 = organizationDAO.getById(3L).get();
        Organization o4 = organizationDAO.getById(4L).get();
        List<Organization> forProduct1 = List.of(o3, o1, o2, o4);
        List<Organization> forProduct2 = List.of(o2, o1, o3, o4);
        List<Organization> forProduct3 = List.of(o1, o2, o3, o4);
        List<Organization> forProduct4 = List.of(o1, o2, o3, o4);


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

        Organization o1 = organizationDAO.getById(1L).get();
        Organization o2 = organizationDAO.getById(2L).get();
        Organization o3 = organizationDAO.getById(3L).get();
        Organization o4 = organizationDAO.getById(4L).get();

        assertTrue(organizationDAO.findOrganizationAmountProductMoreThanValue(testData1).contains(o1));
        assertTrue(organizationDAO.findOrganizationAmountProductMoreThanValue(testData2).contains(o3));
        assertTrue(organizationDAO.findOrganizationAmountProductMoreThanValue(testData3).isEmpty());
        assertTrue(organizationDAO.findOrganizationAmountProductMoreThanValue(testData4).containsAll(List.of(o1, o2,o3)));
    }

}

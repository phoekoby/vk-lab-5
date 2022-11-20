import org.example.dto.AmountSumDTO;
import org.example.dto.PeriodAmountSumAndResultProductDTO;
import org.example.entity.OrganizationDTO;
import org.example.entity.ProductDTO;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDTODAOTest extends AbstractDataBaseTest {

    @Test
    public void getAll() {
        Collection<ProductDTO> productDTOS = productDAO.getAll();
        assertTrue(productDTOS.containsAll(
                List.of(
                        new ProductDTO(1L, "Product 1", 1L),
                        new ProductDTO(2L, "Product 2", 2L),
                        new ProductDTO(3L, "Product 3", 3L),
                        new ProductDTO(4L, "Product 4", 4L)
                )));
        assertEquals(productDTOS.size(), 4);
    }

    @Test
    public void getById() {
        Long existingId = 1L;
        Long notExistingId = 7L;
        Optional<ProductDTO> product = productDAO.getById(existingId);
        assertTrue(product.isPresent());
        assertEquals(product.get(), new ProductDTO(1L, "Product 1", 1L));
        Optional<ProductDTO> product1 = productDAO.getById(notExistingId);
        assertTrue(product1.isEmpty());
    }

    @Test
    public void delete() {
        Long existingId = 1L;
        Optional<ProductDTO> product = productDAO.getById(existingId);
        assertTrue(product.isPresent());
        productDAO.delete(existingId);
        product = productDAO.getById(existingId);
        assertTrue(product.isEmpty());
    }

    @Test
    public void update() {
        ProductDTO existing = new ProductDTO(1L, "Product 1", 1L);
        ProductDTO existingChanged = new ProductDTO(1L, "New Product 1", 1111L);
        ProductDTO notExisting = new ProductDTO(7L, "Product 7", 7L);

        Optional<ProductDTO> product = productDAO.getById(existing.getId());
        assertTrue(product.isPresent());
        assertEquals(existing, product.get());
        productDAO.update(existingChanged);
        product = productDAO.getById(existing.getId());
        assertTrue(product.isPresent());
        assertEquals(product.get(), existingChanged);

        assertThrows(IllegalArgumentException.class, () -> productDAO.update(notExisting));
    }

    @Test
    public void save() {
        int dbSizeBefore = 4;
        ProductDTO notExisting = new ProductDTO("Product 5", 5L);
        Optional<ProductDTO> product = productDAO.getById(5L);
        assertTrue(product.isEmpty());

        productDAO.save(notExisting);
        product = productDAO.getById(5L);
        assertTrue(product.isPresent());
        assertEquals(productDAO.getAll().size(), dbSizeBefore + 1);
    }

    @Test
    public void saveAll() {
        int dbSizeBefore = productDAO.getAll().size();
        int sizeDbShouldBeAfterSaving = dbSizeBefore + 2;
        ProductDTO notExisting1 = new ProductDTO("Product 5", 5L);
        ProductDTO notExisting2 = new ProductDTO("Product 6", 6L);
        Collection<ProductDTO> notExistingOrganizations = List.of(notExisting1, notExisting2);
        Collection<ProductDTO> result = productDAO.saveAll(notExistingOrganizations);
        assertEquals(result.size(), 2);
        assertEquals(sizeDbShouldBeAfterSaving, productDAO.getAll().size());
    }

    @Test
    public void getAmountAndSumPerDayAndResultForPeriod() {
        Date floor = Date.valueOf("2022-11-12");
        Date roof = Date.valueOf("2022-11-14");
        List<Date> resultDays = List.of(floor, Date.valueOf("2022-11-13"), roof);
        PeriodAmountSumAndResultProductDTO result = productDAO.getAmountAndSumPerDayAndResultForPeriod(floor, roof);
        Map<Date, Map<ProductDTO, AmountSumDTO>> perDay = result.getProductsPerDay();
        assertTrue(perDay.keySet().containsAll(resultDays));
        Map<ProductDTO, AmountSumDTO> map1 = perDay.get(resultDays.get(0));
        ProductDTO productDTO1 = productDAO.getById(1L).get();
        assertTrue(map1.containsKey(productDTO1));
        assertEquals(map1.get(productDTO1).getAmount(), 11000);
        assertEquals(map1.get(productDTO1).getSum(), 1350000);

        Map<ProductDTO, AmountSumDTO> map2 = perDay.get(resultDays.get(1));
        ProductDTO productDTO2 = productDAO.getById(1L).get();
        assertTrue(map2.containsKey(productDTO2));
        assertEquals(map2.get(productDTO2).getAmount(), 10000);
        assertEquals(map2.get(productDTO2).getSum(), 1000000);

        Map<ProductDTO, AmountSumDTO> map3 = perDay.get(resultDays.get(2));
        ProductDTO productDTO3 = productDAO.getById(2L).get();
        assertTrue(map3.containsKey(productDTO3));
        assertEquals(map3.get(productDTO3).getAmount(), 1000);
        assertEquals(map3.get(productDTO3).getSum(), 100000);

        Map<ProductDTO, AmountSumDTO> resultPerProduct = result.getAmountSumResultForPeriod();
        assertTrue(resultPerProduct.containsKey(productDTO1));
        assertTrue(resultPerProduct.containsKey(productDTO3));

        assertEquals(resultPerProduct.get(productDTO1).getSum(), 2350000);
        assertEquals(resultPerProduct.get(productDTO1).getAmount(), 21000);

        assertEquals(resultPerProduct.get(productDTO3).getAmount(), 1000);
        assertEquals(resultPerProduct.get(productDTO3).getSum(), 100000);
    }


    @Test
    public void getAveragePriceForProductsInPeriod() {
        Date floor = Date.valueOf("2022-11-12");
        Date roof = Date.valueOf("2022-11-14");
        Map<ProductDTO, Double> averagePrice = productDAO.getAveragePriceForProductsInPeriod(floor, roof);
        ProductDTO productDTO1 = productDAO.getById(1L).get();
        ProductDTO productDTO2 = productDAO.getById(2L).get();

        assertTrue(averagePrice.containsKey(productDTO1));
        assertTrue(averagePrice.containsKey(productDTO2));

        assertEquals(averagePrice.get(productDTO1), 108.333, 0.001);
        assertEquals(averagePrice.get(productDTO2), 100.0, 0.001);
    }

    @Test
    public void getDeliveredProductsByOrganizationInPeriod() {
        Date floor = Date.valueOf("2022-11-09");
        Date roof = Date.valueOf("2022-11-12");
        Map<OrganizationDTO, List<ProductDTO>> result = productDAO.getDeliveredProductsByOrganizationInPeriod(floor, roof);
        Map<OrganizationDTO, List<ProductDTO>> shouldBe = new HashMap<>();
        shouldBe.put(organizationDAO.getById(1L).get(), List.of(
                productDAO.getById(1L).get(),
                productDAO.getById(2L).get(),
                productDAO.getById(3L).get()));
        shouldBe.put(organizationDAO.getById(2L).get(), List.of(
                productDAO.getById(1L).get()));
        shouldBe.put(organizationDAO.getById(3L).get(), List.of(
                productDAO.getById(1L).get()));
        shouldBe.put(organizationDAO.getById(4L).get(), new ArrayList<>());

        for (
                Map.Entry<OrganizationDTO, List<ProductDTO>> entry : result.entrySet()
        ) {
            OrganizationDTO organizationDTO = entry.getKey();
            assertTrue(shouldBe.get(organizationDTO).containsAll(entry.getValue()));
        }

    }

}

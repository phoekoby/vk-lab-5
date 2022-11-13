import org.example.dto.AmountSumDTO;
import org.example.dto.PeriodAmountSumAndResultProductDTO;
import org.example.entity.Organization;
import org.example.entity.Product;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ProductDAOTest extends AbstractDataBaseTest {

    @Test
    public void getAll() {
        Collection<Product> products = productDAO.getAll();
        assertTrue(products.containsAll(
                List.of(
                        new Product(1L, "Product 1", 1L),
                        new Product(2L, "Product 2", 2L),
                        new Product(3L, "Product 3", 3L),
                        new Product(4L, "Product 4", 4L)
                )));
        assertEquals(products.size(), 4);
    }

    @Test
    public void getById() {
        Long existingId = 1L;
        Long notExistingId = 7L;
        Optional<Product> product = productDAO.getById(existingId);
        assertTrue(product.isPresent());
        assertEquals(product.get(), new Product(1L, "Product 1", 1L));
        Optional<Product> product1 = productDAO.getById(notExistingId);
        assertTrue(product1.isEmpty());
    }

    @Test
    public void delete() {
        Long existingId = 1L;
        Optional<Product> product = productDAO.getById(existingId);
        assertTrue(product.isPresent());
        productDAO.delete(existingId);
        product = productDAO.getById(existingId);
        assertTrue(product.isEmpty());
    }

    @Test
    public void update() {
        Product existing = new Product(1L, "Product 1", 1L);
        Product existingChanged = new Product(1L, "New Product 1", 1111L);
        Product notExisting = new Product(7L, "Product 7", 7L);

        Optional<Product> product = productDAO.getById(existing.getId());
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
        Product notExisting = new Product("Product 5", 5L);
        Optional<Product> product = productDAO.getById(5L);
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
        Product notExisting1 = new Product("Product 5", 5L);
        Product notExisting2 = new Product("Product 6", 6L);
        Collection<Product> notExistingOrganizations = List.of(notExisting1, notExisting2);
        Collection<Product> result = productDAO.saveAll(notExistingOrganizations);
        assertEquals(result.size(), 2);
        assertEquals(sizeDbShouldBeAfterSaving, productDAO.getAll().size());
    }

    @Test
    public void getAmountAndSumPerDayAndResultForPeriod() {
        Date floor = Date.valueOf("2022-11-12");
        Date roof = Date.valueOf("2022-11-14");
        List<Date> resultDays = List.of(floor, Date.valueOf("2022-11-13"), roof);
        PeriodAmountSumAndResultProductDTO result = productDAO.getAmountAndSumPerDayAndResultForPeriod(floor, roof);
        Map<Date, Map<Product, AmountSumDTO>> perDay = result.getProductsPerDay();
        assertTrue(perDay.keySet().containsAll(resultDays));
        Map<Product, AmountSumDTO> map1 = perDay.get(resultDays.get(0));
        Product product1 = productDAO.getById(1L).get();
        assertTrue(map1.containsKey(product1));
        assertEquals(map1.get(product1).getAmount(), 11000);
        assertEquals(map1.get(product1).getSum(), 1350000);

        Map<Product, AmountSumDTO> map2 = perDay.get(resultDays.get(1));
        Product product2 = productDAO.getById(1L).get();
        assertTrue(map2.containsKey(product2));
        assertEquals(map2.get(product2).getAmount(), 10000);
        assertEquals(map2.get(product2).getSum(), 1000000);

        Map<Product, AmountSumDTO> map3 = perDay.get(resultDays.get(2));
        Product product3 = productDAO.getById(2L).get();
        assertTrue(map3.containsKey(product3));
        assertEquals(map3.get(product3).getAmount(), 1000);
        assertEquals(map3.get(product3).getSum(), 100000);

        Map<Product, AmountSumDTO> resultPerProduct = result.getAmountSumResultForPeriod();
        assertTrue(resultPerProduct.containsKey(product1));
        assertTrue(resultPerProduct.containsKey(product3));

        assertEquals(resultPerProduct.get(product1).getSum(), 2350000);
        assertEquals(resultPerProduct.get(product1).getAmount(), 21000);

        assertEquals(resultPerProduct.get(product3).getAmount(), 1000);
        assertEquals(resultPerProduct.get(product3).getSum(), 100000);
    }


    @Test
    public void getAveragePriceForProductsInPeriod() {
        Date floor = Date.valueOf("2022-11-12");
        Date roof = Date.valueOf("2022-11-14");
        Map<Product, Double> averagePrice = productDAO.getAveragePriceForProductsInPeriod(floor, roof);
        Product product1 = productDAO.getById(1L).get();
        Product product2 = productDAO.getById(2L).get();

        assertTrue(averagePrice.containsKey(product1));
        assertTrue(averagePrice.containsKey(product2));

        assertEquals(averagePrice.get(product1), 108.333, 0.001);
        assertEquals(averagePrice.get(product2), 100.0, 0.001);
    }

    @Test
    public void getDeliveredProductsByOrganizationInPeriod() {
        Date floor = Date.valueOf("2022-11-09");
        Date roof = Date.valueOf("2022-11-12");
        Map<Organization, List<Product>> result = productDAO.getDeliveredProductsByOrganizationInPeriod(floor, roof);
        Map<Organization, List<Product>> shouldBe = new HashMap<>();
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
                Map.Entry<Organization, List<Product>> entry : result.entrySet()
        ) {
            Organization organization = entry.getKey();
            assertTrue(shouldBe.get(organization).containsAll(entry.getValue()));
        }

    }

}

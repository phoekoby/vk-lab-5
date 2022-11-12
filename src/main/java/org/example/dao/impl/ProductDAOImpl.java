package org.example.dao.impl;

import org.example.dao.ProductDAO;
import org.example.dto.AmountSumDTO;
import org.example.dto.PeriodAmountSumAndResultProductDTO;
import org.example.entity.Organization;
import org.example.entity.Product;
import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

import static org.example.config.DbCredentials.*;

@SuppressWarnings("FieldCanBeLocal")
public class ProductDAOImpl implements ProductDAO {


    private final String sqlGetAllProducts = "SELECT * FROM product;";

    private final String sqlGetByIdProduct = """
            SELECT * FROM product where id=?;
            """;

    private final String sqlDeleteProductById = """
            DELETE FROM product where id=?;
            """;

    private final String sqlUpdateProduct = """
            UPDATE product SET name=?, internal_code=? WHERE id=?;
             """;

    private final String sqlInsert = """
            INSERT INTO product(name, internal_code)  VALUES (?, ?);
            """;

    private final String sqlAmountSumInPeriod = """
            SELECT i.invoice_date,
                   p.id                      as product_id,
                   p.internal_code,
                   p.name,
                   sum(ip.amount)            as amount,
                   sum(ip.amount * ip.price) as sum
            FROM invoice_position ip
                     LEFT JOIN product p on p.id = ip.product_id
                     RIGHT JOIN invoice i on i.id = ip.invoice_id
            WHERE i.invoice_date >= ?
              AND i.invoice_date <= ?
            GROUP BY i.invoice_date, p.id
            ORDER BY i.invoice_date;
            """;
    private final String sqlAmountSumResultInPeriod = """
            SELECT p.id, p.internal_code, p.name, sum(ip.amount) as amount, sum(ip.amount * ip.price) as sum
            FROM invoice_position ip
                     LEFT JOIN product p on p.id = ip.product_id
                     RIGHT JOIN invoice i on i.id = ip.invoice_id
            WHERE i.invoice_date >= ?
              AND i.invoice_date <= ?
            GROUP BY p.id;
            """;

    private final String sqlAveragePriceForProductInPeriod = """
            SELECT p.id, p.name, p.internal_code, avg(ip.price)
            FROM invoice_position ip
                     LEFT JOIN product p on p.id = ip.product_id
                     RIGHT JOIN invoice i on i.id = ip.invoice_id
            WHERE i.invoice_date >= ?
              AND i.invoice_date <= ?
            GROUP BY p.id;
            """;

    private final String sqlOrganizationsWithDeliveredProducts = """
            SELECT o.id,
                   o.inn,
                   o.checking_account,
                   ARRAY((SELECT p.id
                          FROM product p
                          WHERE p.id in (SELECT ip.product_id
                                         FROM invoice_position ip
                                         WHERE ip.invoice_id in (SELECT i.id
                                                                 FROM invoice i
                                                                 WHERE i.invoice_date >= ?
                                                                   AND i.invoice_date <= ?
                                                                   AND i.sender_id = o.id)))) as products
            FROM organization o;
            """;

    @Override
    public List<Product> getAll() {
        try (var connection = DriverManager.getConnection(CONNECTION + DB_NAME, USERNAME, PASSWORD)) {
            try (PreparedStatement statement = connection.prepareStatement(sqlGetAllProducts)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    return collectToListProducts(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Product> getById(@NotNull Long id) {
        try (var connection = DriverManager.getConnection(CONNECTION + DB_NAME, USERNAME, PASSWORD)) {
            try (PreparedStatement statement = connection.prepareStatement(sqlGetByIdProduct)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    Product product = null;
                    while (resultSet.next()) {
                        final Long id0 = resultSet.getLong("id");
                        final String name = resultSet.getString("name");
                        final Long internalCode = resultSet.getLong("internal_code");
                        product = new Product(id0, name, internalCode);
                    }
                    return Optional.ofNullable(product);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(@NotNull Long id) {
        try (var connection = DriverManager.getConnection(CONNECTION + DB_NAME, USERNAME, PASSWORD)) {
            try (PreparedStatement statement = connection.prepareStatement(sqlDeleteProductById)) {
                statement.setLong(1, id);
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product update(@NotNull Product value) {
        try (var connection = DriverManager.getConnection(CONNECTION + DB_NAME, USERNAME, PASSWORD)) {
            try (PreparedStatement statement = connection.prepareStatement(sqlUpdateProduct)) {
                statement.setString(1, value.getName());
                statement.setLong(2, value.getInternalCode());
                statement.executeUpdate();
                return value;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product save(@NotNull Product value) {
        try (var connection = DriverManager.getConnection(CONNECTION + DB_NAME, USERNAME, PASSWORD)) {
            try (PreparedStatement statement = connection.prepareStatement(sqlInsert)) {
                statement.setString(1, value.getName());
                statement.setLong(2, value.getInternalCode());
                statement.executeUpdate();
                return value;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<Product> saveAll(@NotNull Collection<Product> values) {
        Collection<Product> result = new ArrayList<>();
        for (Product value : values) {
            result.add(save(value));
        }
        return result;
    }

    @Override
    public PeriodAmountSumAndResultProductDTO getAmountAndSumPerDayAndResultForPeriod(Date floor, Date roof) {
        try (var connection = DriverManager.getConnection(CONNECTION + DB_NAME, USERNAME, PASSWORD)) {
            final Map<Date, Map<Product, AmountSumDTO>> productPerDay = productPerDay(connection, floor, roof);
            final Map<Product, AmountSumDTO> amountSumResultForPeriod = amountSumResultForPeriod(connection, floor, roof);
            return new PeriodAmountSumAndResultProductDTO(productPerDay, amountSumResultForPeriod);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<Product, Double> getAveragePriceForProductsInPeriod(Date floor, Date roof) {
        try (var connection = DriverManager.getConnection(CONNECTION + DB_NAME, USERNAME, PASSWORD)) {
            final Map<Product, Double> productWithAveragePrice = new HashMap<>();
            try (PreparedStatement statement = connection.prepareStatement(sqlAveragePriceForProductInPeriod)) {
                statement.setDate(1, floor);
                statement.setDate(2, roof);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        final Long productId = resultSet.getLong("id");
                        final Long internalCode = resultSet.getLong("internal_code");
                        final String name = resultSet.getString("name");
                        final Double averagePrice = resultSet.getDouble("avg");
                        Product product = new Product(productId, name, internalCode);
                        productWithAveragePrice.put(product, averagePrice);
                    }
                }
                return productWithAveragePrice;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Map<Organization, List<Product>> getDeliveredProductsByOrganizationInPeriod(Date floor, Date roof) {
        try (var connection = DriverManager.getConnection(CONNECTION + DB_NAME, USERNAME, PASSWORD)) {
            final Map<Organization, List<Product>> organizationsWithDeliveredProducts = new TreeMap<>();
            try (PreparedStatement statement = connection.prepareStatement(sqlOrganizationsWithDeliveredProducts)) {
                statement.setDate(1, floor);
                statement.setDate(2, roof);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        final Long organizationId = resultSet.getLong("id");
                        final Long inn = resultSet.getLong("inn");
                        final Long checkingAccount = resultSet.getLong("checking_account");
                        final Array products = resultSet.getArray("products");
                        Organization organization = new Organization(organizationId, inn, checkingAccount);
                        List<Product> productsList;
                        Long[] productsId = (Long[]) products.getArray();
                        productsList = Arrays
                                .stream(productsId)
                                .map(this::getById)
                                .map(Optional::get)
                                .collect(Collectors.toList());
                        organizationsWithDeliveredProducts.put(organization, productsList);
                    }
                }
                return organizationsWithDeliveredProducts;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<Date, Map<Product, AmountSumDTO>> productPerDay(Connection connection, Date floor, Date roof) throws SQLException {
        final Map<Date, Map<Product, AmountSumDTO>> productPerDay = new TreeMap<>();
        try (PreparedStatement statement = connection.prepareStatement(sqlAmountSumInPeriod)) {
            statement.setDate(1, floor);
            statement.setDate(2, roof);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    final Date invoiceDate = resultSet.getDate("invoice_date");
                    final Long productId = resultSet.getLong("product_id");
                    final Long internalCode = resultSet.getLong("internal_code");
                    final String name = resultSet.getString("name");
                    final Integer amount = resultSet.getInt("amount");
                    final Double sum = resultSet.getDouble("sum");
                    if (!productPerDay.containsKey(invoiceDate)) {
                        productPerDay.put(invoiceDate, new HashMap<>());
                    }
                    Map<Product, AmountSumDTO> amountAndSumProduct = productPerDay.get(invoiceDate);
                    Product product = new Product(productId, name, internalCode);
                    amountAndSumProduct.put(product, new AmountSumDTO(amount, sum));
                }

            }
            return productPerDay;
        }
    }

    private Map<Product, AmountSumDTO> amountSumResultForPeriod(Connection connection, Date floor, Date roof) throws SQLException {
        final Map<Product, AmountSumDTO> amountSumResultForPeriod = new HashMap<>();
        try (PreparedStatement statement = connection.prepareStatement(sqlAmountSumResultInPeriod)) {
            statement.setDate(1, floor);
            statement.setDate(2, roof);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    final Long productId = resultSet.getLong("id");
                    final Long internalCode = resultSet.getLong("internal_code");
                    final String name = resultSet.getString("name");
                    final Integer amount = resultSet.getInt("amount");
                    final Double sum = resultSet.getDouble("sum");
                    Product product = new Product(productId, name, internalCode);
                    amountSumResultForPeriod.put(product, new AmountSumDTO(amount, sum));
                }
            }
            return amountSumResultForPeriod;
        }
    }

    private List<Product> collectToListProducts(ResultSet resultSet) throws SQLException {
        List<Product> result = new ArrayList<>();
        while (resultSet.next()) {
            final Long id = resultSet.getLong("id");
            final String name = resultSet.getString("name");
            final Long internalCode = resultSet.getLong("internal_code");
            result.add(new Product(id, name, internalCode));
        }
        return result;
    }
}

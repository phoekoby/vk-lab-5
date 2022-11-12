package org.example.dao.impl;

import org.example.dao.ProductDAO;
import org.example.entity.Invoice;
import org.example.entity.Product;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.example.config.DbCredentials.*;
import static org.example.config.DbCredentials.PASSWORD;

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
                statement.setLong(0, id);
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
                statement.setLong(0, id);
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

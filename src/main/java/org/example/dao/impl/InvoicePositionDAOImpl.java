package org.example.dao.impl;

import org.example.dao.InvoicePositionDAO;
import org.example.entity.Invoice;
import org.example.entity.InvoicePosition;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.example.config.DbCredentials.*;
import static org.example.config.DbCredentials.PASSWORD;

public class InvoicePositionDAOImpl implements InvoicePositionDAO {

    private final String sqlGetAllInvoicePosition = "SELECT * FROM invoice_position;";

    private final String sqlGetByIdInvoicePosition = """
            SELECT * FROM invoice_position where id=?;
            """;

    private final String sqlDeleteInvoicePositionById = """
            DELETE FROM invoice_position where id=?;
            """;

    private final String sqlUpdateInvoicePosition = """
            UPDATE invoice_position SET price=?,amount=?, product_id=?, invoice_id=?WHERE id=?;
             """;

    private final String sqlInsert = """
            INSERT INTO invoice_position(price,amount, product_id, invoice_id) VALUES (?, ?, ?, ?);
            """;

    @Override
    public List<InvoicePosition> getAll() {
        try (var connection = DriverManager.getConnection(CONNECTION + DB_NAME, USERNAME, PASSWORD)) {
            try (PreparedStatement statement = connection.prepareStatement(sqlGetAllInvoicePosition)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    return collectToListInvoicePositions(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<InvoicePosition> getById(@NotNull Long id) {
        try (var connection = DriverManager.getConnection(CONNECTION + DB_NAME, USERNAME, PASSWORD)) {
            try (PreparedStatement statement = connection.prepareStatement(sqlGetByIdInvoicePosition)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    InvoicePosition invoicePosition = null;
                    while (resultSet.next()) {
                        final Long id0 = resultSet.getLong("id");
                        final Double price = resultSet.getDouble("price");
                        final Integer amount = resultSet.getInt("amount");
                        final Long productId = resultSet.getLong("product_id");
                        final Long invoiceId = resultSet.getLong("invoice_id");
                        invoicePosition = new InvoicePosition(id0, price, amount, productId, invoiceId);
                    }
                    return Optional.ofNullable(invoicePosition);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(@NotNull Long id) {
        try (var connection = DriverManager.getConnection(CONNECTION + DB_NAME, USERNAME, PASSWORD)) {
            try (PreparedStatement statement = connection.prepareStatement(sqlDeleteInvoicePositionById)) {
                statement.setLong(1, id);
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InvoicePosition update(@NotNull InvoicePosition value) {
        try (var connection = DriverManager.getConnection(CONNECTION + DB_NAME, USERNAME, PASSWORD)) {
            try (PreparedStatement statement = connection.prepareStatement(sqlUpdateInvoicePosition)) {
                statement.setDouble(1, value.getPrice());
                statement.setInt(2, value.getAmount());
                statement.setLong(3, value.getProductId());
                statement.setLong(4, value.getInvoiceId());
                statement.executeUpdate();
                return value;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InvoicePosition save(@NotNull InvoicePosition value) {
        try (var connection = DriverManager.getConnection(CONNECTION + DB_NAME, USERNAME, PASSWORD)) {
            try (PreparedStatement statement = connection.prepareStatement(sqlInsert)) {
                statement.setDouble(1, value.getPrice());
                statement.setInt(2, value.getAmount());
                statement.setLong(3, value.getProductId());
                statement.setLong(4, value.getInvoiceId());
                statement.executeUpdate();
                return value;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<InvoicePosition> saveAll(@NotNull Collection<InvoicePosition> values) {
        Collection<InvoicePosition> result = new ArrayList<>();
        for (InvoicePosition value : values) {
            result.add(save(value));
        }
        return result;
    }


    private List<InvoicePosition> collectToListInvoicePositions(ResultSet resultSet) throws SQLException {
        List<InvoicePosition> result = new ArrayList<>();
        while (resultSet.next()) {
            final Long id = resultSet.getLong("id");
            final Double price = resultSet.getDouble("price");
            final Integer amount = resultSet.getInt("amount");
            final Long productId = resultSet.getLong("product_id");
            final Long invoiceId = resultSet.getLong("invoice_id");
            result.add(new InvoicePosition(id, price, amount, productId, invoiceId));
        }
        return result;
    }
}

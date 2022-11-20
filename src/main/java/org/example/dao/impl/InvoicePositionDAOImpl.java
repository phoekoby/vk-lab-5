package org.example.dao.impl;

import com.google.inject.Inject;
import org.example.config.DBCredentials;
import org.example.dao.InvoicePositionDAO;
import org.example.entity.InvoicePositionDTO;
import org.jetbrains.annotations.NotNull;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("FieldCanBeLocal")
public class InvoicePositionDAOImpl implements InvoicePositionDAO {

    private final String sqlGetAllInvoicePosition = "SELECT * FROM invoice_position;";

    private final String sqlGetByIdInvoicePosition = """
            SELECT * FROM invoice_position where id=?;
            """;

    private final String sqlDeleteInvoicePositionById = """
            DELETE FROM invoice_position where id=?;
            """;

    private final String sqlUpdateInvoicePosition = """
            UPDATE invoice_position SET price=?,amount=?, product_id=?, invoice_id=? WHERE id=?;
             """;

    private final String sqlInsert = """
            INSERT INTO invoice_position(price,amount, product_id, invoice_id) VALUES (?, ?, ?, ?);
            """;
    private final DBCredentials dbCredentials;

    @Inject
    public InvoicePositionDAOImpl(DBCredentials dbCredentials) {
        this.dbCredentials = dbCredentials;
    }
    @Override
    public List<InvoicePositionDTO> getAll() {
        try (var connection = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
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
    public Optional<InvoicePositionDTO> getById(@NotNull Long id) {
        try (var connection = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            try (PreparedStatement statement = connection.prepareStatement(sqlGetByIdInvoicePosition)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    InvoicePositionDTO invoicePositionDTO = null;
                    while (resultSet.next()) {
                        final Long id0 = resultSet.getLong("id");
                        final Double price = resultSet.getDouble("price");
                        final Integer amount = resultSet.getInt("amount");
                        final Long productId = resultSet.getLong("product_id");
                        final Long invoiceId = resultSet.getLong("invoice_id");
                        invoicePositionDTO = new InvoicePositionDTO(id0, price, amount, productId, invoiceId);
                    }
                    return Optional.ofNullable(invoicePositionDTO);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(@NotNull Long id) {
        try (var connection = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            try (PreparedStatement statement = connection.prepareStatement(sqlDeleteInvoicePositionById)) {
                statement.setLong(1, id);
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InvoicePositionDTO update(@NotNull InvoicePositionDTO value) {
        if(value.getId() == null || getById(value.getId()).isEmpty()){
            throw new IllegalArgumentException("Row with this id is not existing");
        }
        try (var connection = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            try (PreparedStatement statement = connection.prepareStatement(sqlUpdateInvoicePosition)) {
                statement.setDouble(1, value.getPrice());
                statement.setInt(2, value.getAmount());
                statement.setLong(3, value.getProductId());
                statement.setLong(4, value.getInvoiceId());
                statement.setLong(5, value.getId());
                statement.executeUpdate();
                return value;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InvoicePositionDTO save(@NotNull InvoicePositionDTO value) {
        try (var connection = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
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
    public Collection<InvoicePositionDTO> saveAll(@NotNull Collection<InvoicePositionDTO> values) {
        Collection<InvoicePositionDTO> result = new ArrayList<>();
        for (InvoicePositionDTO value : values) {
            result.add(save(value));
        }
        return result;
    }


    private List<InvoicePositionDTO> collectToListInvoicePositions(ResultSet resultSet) throws SQLException {
        List<InvoicePositionDTO> result = new ArrayList<>();
        while (resultSet.next()) {
            final Long id = resultSet.getLong("id");
            final Double price = resultSet.getDouble("price");
            final Integer amount = resultSet.getInt("amount");
            final Long productId = resultSet.getLong("product_id");
            final Long invoiceId = resultSet.getLong("invoice_id");
            result.add(new InvoicePositionDTO(id, price, amount, productId, invoiceId));
        }
        return result;
    }
}

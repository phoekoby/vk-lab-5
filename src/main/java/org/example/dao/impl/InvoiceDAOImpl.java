package org.example.dao.impl;

import com.google.inject.Inject;
import org.example.config.DBCredentials;
import org.example.dao.InvoiceDAO;
import org.example.entity.InvoiceDTO;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("FieldCanBeLocal")
public class InvoiceDAOImpl implements InvoiceDAO {
    private final String sqlGetAllInvoices = "SELECT * FROM invoice;";

    private final String sqlGetByIdInvoice = """
            SELECT * FROM invoice where id=?;
            """;

    private final String sqlDeleteInvoiceById = """
            DELETE FROM invoice where id=?;
            """;

    private final String sqlUpdateInvoice = """
            UPDATE invoice SET number=?, invoice_date=?, sender_id=? WHERE id=?;
             """;

    private final String sqlInsert = """
            INSERT INTO invoice(number, invoice_date, sender_id)  VALUES (?, ?, ?);
            """;

    private final DBCredentials dbCredentials;

    @Inject
    public InvoiceDAOImpl(DBCredentials dbCredentials) {
        this.dbCredentials = dbCredentials;
    }

    @Override
    public List<InvoiceDTO> getAll() {
        try (var connection = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            try (PreparedStatement statement = connection.prepareStatement(sqlGetAllInvoices)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    return collectToListInvoices(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<InvoiceDTO> getById(@NotNull Long id) {
        try (var connection = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            try (PreparedStatement statement = connection.prepareStatement(sqlGetByIdInvoice)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    InvoiceDTO invoiceDTO = null;
                    while (resultSet.next()) {
                        final Long id0 = resultSet.getLong("id");
                        final Long number = resultSet.getLong("number");
                        final Date invoiceDate = resultSet.getDate("invoice_date");
                        final Long senderId = resultSet.getLong("sender_id");
                        invoiceDTO = new InvoiceDTO(id0, number, invoiceDate, senderId);
                    }
                    return Optional.ofNullable(invoiceDTO);
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
            try (PreparedStatement statement = connection.prepareStatement(sqlDeleteInvoiceById)) {
                statement.setLong(1, id);
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InvoiceDTO update(@NotNull InvoiceDTO value) {
        if(value.getId() == null || getById(value.getId()).isEmpty()){
            throw new IllegalArgumentException("Row with this id is not existing");
        }
        try (var connection = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            try (PreparedStatement statement = connection.prepareStatement(sqlUpdateInvoice)) {
                statement.setLong(1, value.getNumber());
                statement.setDate(2, value.getInvoiceDate());
                statement.setLong(3, value.getSenderId());
                statement.setLong(4, value.getId());
                statement.executeUpdate();
                return value;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InvoiceDTO save(@NotNull InvoiceDTO value) {
        try (var connection = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            try (PreparedStatement statement = connection.prepareStatement(sqlInsert)) {
                statement.setLong(1, value.getNumber());
                statement.setDate(2, value.getInvoiceDate());
                statement.setLong(3, value.getSenderId());
                statement.executeUpdate();
                return value;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<InvoiceDTO> saveAll(@NotNull Collection<InvoiceDTO> values) {
        Collection<InvoiceDTO> result = new ArrayList<>();
        for (InvoiceDTO value : values) {
            result.add(save(value));
        }
        return result;
    }


    private List<InvoiceDTO> collectToListInvoices(ResultSet resultSet) throws SQLException {
        List<InvoiceDTO> result = new ArrayList<>();
        while (resultSet.next()) {
            final Long id = resultSet.getLong("id");
            final Long number = resultSet.getLong("number");
            final Date invoiceDate = resultSet.getDate("invoice_date");
            final Long senderId = resultSet.getLong("sender_id");
            result.add(new InvoiceDTO(id, number, invoiceDate, senderId));
        }
        return result;
    }
}

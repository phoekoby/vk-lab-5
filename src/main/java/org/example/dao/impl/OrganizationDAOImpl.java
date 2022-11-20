package org.example.dao.impl;

import com.google.inject.Inject;
import org.example.config.DBCredentials;
import org.example.dao.OrganizationDAO;
import org.example.entity.OrganizationDTO;
import org.jetbrains.annotations.NotNull;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@SuppressWarnings("FieldCanBeLocal")
public class OrganizationDAOImpl implements OrganizationDAO {
    private final String sqlGetAllOrganizations = """
            SELECT * FROM organization;
            """;

    private final String sqlGetByIdOrganization = """
            SELECT * FROM organization where id=?;
            """;

    private final String sqlDeleteOrganizationById = """
            DELETE FROM organization where id=?;
            """;

    private final String sqlUpdateOrganization = """
            UPDATE organization SET inn=?, checking_account=? WHERE id=?;
             """;

    private final String sqlInsert = """
            INSERT INTO organization(inn, checking_account) VALUES (?, ?);
            """;
    private final String sqlFindFirst10OrganizationsByProduct = """
            SELECT *
            FROM organization org
            ORDER BY (SELECT coalesce(sum(amount), 0)
                      from invoice_position
                      where invoice_id in
                            (SELECT id
                             from invoice inv
                             where org.id = inv.sender_id) and product_id = ?) DESC
            LIMIT 10;""";


    private final String sqlFindOrganizationAmountProductMoreThanValue = """
            SELECT *
            FROM organization org
            WHERE ((SELECT coalesce(sum(amount), 0)
                    from invoice_position
                    where invoice_id in
                          (SELECT id
                           from invoice inv
                           where org.id = inv.sender_id) and product_id = ?) > ?)""";

    private final String secondPartOfSqlFindOrganizationAmountProductMoreThanValue = """
             and ((SELECT coalesce(sum(amount), 0)
                  from invoice_position
                  where invoice_id in
                        (SELECT id
                         from invoiceDTO inv
                         where org.id = inv.sender_id) and product_id = ?) > ?)
            """;

    private final DBCredentials dbCredentials;

    @Inject
    public OrganizationDAOImpl(DBCredentials dbCredentials) {
        this.dbCredentials = dbCredentials;
    }
    @Override
    public Collection<OrganizationDTO> getAll() {
        try (var connection = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            try (PreparedStatement statement = connection.prepareStatement(sqlGetAllOrganizations)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    return collectToListOrganizations(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<OrganizationDTO> getById(@NotNull Long id) {
        try (var connection = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            try (PreparedStatement statement = connection.prepareStatement(sqlGetByIdOrganization)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    OrganizationDTO organizationDTO = null;
                    while (resultSet.next()) {
                        final Long idO = resultSet.getLong("id");
                        final Long inn = resultSet.getLong("inn");
                        final Long checkingAccount = resultSet.getLong("checking_account");
                        organizationDTO = new OrganizationDTO(idO, inn, checkingAccount);
                    }
                    return Optional.ofNullable(organizationDTO);
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
            try (PreparedStatement statement = connection.prepareStatement(sqlDeleteOrganizationById)) {
                statement.setLong(1, id);
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public OrganizationDTO update(@NotNull OrganizationDTO value) {
        if(value.getId() == null || getById(value.getId()).isEmpty()){
            throw new IllegalArgumentException("Row with this id is not existing");
        }
        try (var connection = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            try (PreparedStatement statement = connection.prepareStatement(sqlUpdateOrganization)) {
                statement.setLong(1, value.getINN());
                statement.setLong(2, value.getCheckingAccount());
                statement.setLong(3, value.getId());
                statement.executeUpdate();
                return value;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public OrganizationDTO save(@NotNull OrganizationDTO value) {
        try (var connection = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            try (PreparedStatement statement = connection.prepareStatement(sqlInsert)) {
                statement.setLong(1, value.getINN());
                statement.setLong(2, value.getCheckingAccount());
                statement.executeUpdate();
                return value;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<OrganizationDTO> saveAll(@NotNull Collection<OrganizationDTO> values) {
        Collection<OrganizationDTO> result = new ArrayList<>();
        for (OrganizationDTO value : values) {
            result.add(save(value));
        }
        return result;
    }

    @Override
    public List<OrganizationDTO> findFirst10OrganizationsByProduct(@NotNull Long productId) {
        try (var connection = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            try (PreparedStatement statement = connection.prepareStatement(sqlFindFirst10OrganizationsByProduct)) {
                statement.setLong(1, productId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return collectToListOrganizations(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<OrganizationDTO> findOrganizationAmountProductMoreThanValue(@NotNull Map<Long, Integer> productsWithLimits) {
        if (productsWithLimits.isEmpty()) {
            throw new IllegalArgumentException("Request can not be with empty limits");
        }
        try (var connection = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            Set<Map.Entry<Long, Integer>> entries = productsWithLimits.entrySet();
            final String resultSql = sqlFindOrganizationAmountProductMoreThanValue +
                    secondPartOfSqlFindOrganizationAmountProductMoreThanValue.repeat(Math.max(0, entries.size() - 1)) +
                    ";";

            try (PreparedStatement statement = connection.prepareStatement(resultSql)) {
                int index = 1;
                for (Map.Entry<Long, Integer> entry : entries) {
                    statement.setLong(index, entry.getKey());
                    statement.setLong(index + 1, entry.getValue());
                    index += 2;
                }
                try (ResultSet resultSet = statement.executeQuery()) {
                    return collectToListOrganizations(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private List<OrganizationDTO> collectToListOrganizations(ResultSet resultSet) throws SQLException {
        List<OrganizationDTO> result = new ArrayList<>();
        while (resultSet.next()) {
            final Long id = resultSet.getLong("id");
            final Long inn = resultSet.getLong("inn");
            final Long checkingAccount = resultSet.getLong("checking_account");
            result.add(new OrganizationDTO(id, inn, checkingAccount));
        }
        return result;
    }


}

package org.example.dao.impl;

import static org.example.config.DbCredentials.*;

import org.example.dao.OrganizationDAO;
import org.example.entity.Organization;


import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrganizationDAOImpl implements OrganizationDAO {

//    private final String sqlFindFirst10OrganizationsByProduct = "SELECT *\n" +
//            "FROM organization org\n" +
//            "ORDER BY (SELECT coalesce(sum(amount), 0)\n" +
//            "          from invoice_position\n" +
//            "          where invoice_id in\n" +
//            "                (SELECT id\n" +
//            "                 from invoice inv\n" +
//            "                 where org.id = inv.sender_id)) DESC\n" +
//            "LIMIT 10;";
    private final String sqlFindFirst10OrganizationsByProduct = "SELECT *\n" +
        "FROM organization org\n" +
        "ORDER BY (SELECT coalesce(sum(amount), 0)\n" +
        "          from invoice_position\n" +
        "          where invoice_id in\n" +
        "                (SELECT id\n" +
        "                 from invoice inv\n" +
        "                 where org.id = inv.sender_id) and product_id = ?) DESC\n" +
        "LIMIT 10;";


    private final String sqlFindOrganizationAmountProductMoreThanValue = "SELECT *\n" +
            "FROM organization org\n" +
            "WHERE (SELECT coalesce(sum(amount), 0)\n" +
            "          from invoice_position\n" +
            "          where invoice_id in\n" +
            "                (SELECT id\n" +
            "                 from invoice inv\n" +
            "                 where org.id = inv.sender_id)) > ?;";

    @Override
    public List<Organization> getAll() {
        return null;
    }

    @Override
    public Optional<Organization> getById(Long id) {
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Organization update(Organization value) {
        return null;
    }

    @Override
    public Organization save(Organization value) {
        return null;
    }


    @Override
    public List<Organization> findFirst10OrganizationsByProduct(Long productId) {
        try (var connection = DriverManager.getConnection(CONNECTION + DB_NAME, USERNAME, PASSWORD)) {
            try (PreparedStatement statement = connection.prepareStatement(sqlFindFirst10OrganizationsByProduct)) {
                statement.setLong(0, productId);
                ResultSet resultSet = statement.executeQuery();
                return collectToListOrganizations(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Organization> findOrganizationAmountProductMoreThanValue(int value) {
        try (var connection = DriverManager.getConnection(CONNECTION + DB_NAME, USERNAME, PASSWORD)) {
            try (PreparedStatement statement = connection.prepareStatement(sqlFindOrganizationAmountProductMoreThanValue)) {
                statement.setInt(0, value);
                ResultSet resultSet = statement.executeQuery();
                return collectToListOrganizations(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private List<Organization> collectToListOrganizations(ResultSet resultSet) throws SQLException {
        List<Organization> result = new ArrayList<>();
        while (resultSet.next()) {
            final Long id = resultSet.getLong("id");
            final Long inn = resultSet.getLong("inn");
            final Long checkingAccount = resultSet.getLong("checking_account");
            result.add(new Organization(id, inn, checkingAccount));
        }
        return result;
    }



}

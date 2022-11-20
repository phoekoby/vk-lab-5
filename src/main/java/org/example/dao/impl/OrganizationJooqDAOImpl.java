package org.example.dao.impl;

import com.google.inject.Inject;
import generated.tables.records.OrganizationRecord;
import org.example.config.DBCredentials;
import org.example.dao.OrganizationDAO;
import org.example.entity.OrganizationDTO;
import org.example.mapper.OrganizationMapper;
import org.jetbrains.annotations.NotNull;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static generated.Tables.*;
import static org.jooq.impl.DSL.*;

public class OrganizationJooqDAOImpl implements OrganizationDAO {

    private final DBCredentials dbCredentials;

    private final OrganizationMapper organizationMapper;

    @Inject
    public OrganizationJooqDAOImpl(DBCredentials dbCredentials, OrganizationMapper organizationMapper) {
        this.dbCredentials = dbCredentials;
        this.organizationMapper = organizationMapper;
    }

    @Override
    public Collection<OrganizationDTO> getAll() {
        try (Connection conn = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);

            return context
                    .selectFrom(ORGANIZATION)
                    .fetchStream()
                    .map(organizationMapper::toEntity)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<OrganizationDTO> getById(@NotNull Long id) {
        try (Connection conn = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);

            final @NotNull Optional<OrganizationRecord> record = Optional.ofNullable(context
                    .selectFrom(ORGANIZATION)
                    .where(ORGANIZATION.ID.eq(id))
                    .fetchOne());
            return record.map(organizationMapper::toEntity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(@NotNull Long id) {
        try (Connection conn = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);

            context
                    .deleteFrom(ORGANIZATION)
                    .where(ORGANIZATION.ID.eq(id))
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public OrganizationDTO update(@NotNull OrganizationDTO value) {
        if (value.getId() == null || getById(value.getId()).isEmpty()) {
            throw new IllegalArgumentException("Row with this id is not existing");
        }
        try (Connection conn = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);

            context
                    .update(ORGANIZATION)
                    .set(ORGANIZATION.INN, value.getINN())
                    .set(ORGANIZATION.CHECKING_ACCOUNT, value.getCheckingAccount())
                    .where(ORGANIZATION.ID.eq(value.getId()))
                    .execute();
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public OrganizationDTO save(@NotNull OrganizationDTO value) {
        try (Connection conn = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);

            context.
                    insertInto(ORGANIZATION, ORGANIZATION.INN, ORGANIZATION.CHECKING_ACCOUNT)
                    .values(value.getINN(), value.getCheckingAccount())
                    .execute();
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<OrganizationDTO> findFirst10OrganizationsByProduct(@NotNull Long productId) {
        try (Connection conn = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);
            return context
                    .selectFrom(ORGANIZATION)
                    .orderBy(
                            field(select(coalesce(sum(INVOICE_POSITION.AMOUNT), 0L))
                                    .from(INVOICE_POSITION)
                                    .where(row(INVOICE_POSITION.INVOICE_ID).in(
                                            select(INVOICE.ID)
                                                    .from(INVOICE)
                                                    .where(ORGANIZATION.ID.eq(INVOICE.SENDER_ID))
                                    ).and(INVOICE_POSITION.PRODUCT_ID.eq(productId)))).desc()
                    )
                    .limit(10)
                    .fetchStream()
                    .map(organizationMapper::toEntity)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<OrganizationDTO> findOrganizationAmountProductMoreThanValue(@NotNull Map<Long, Integer> productsWithLimits) {
        if (productsWithLimits.isEmpty()) {
            throw new IllegalArgumentException("Request can not be with empty limits");
        }
        try (Connection conn = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);
            boolean isFirst = true;
            Condition resultCondition = null;
            for (Map.Entry<Long, Integer> entry : productsWithLimits.entrySet()) {
                if (isFirst) {
                    resultCondition = field(select(coalesce(sum(INVOICE_POSITION.AMOUNT), 0L))
                            .from(INVOICE_POSITION)
                            .where(row(INVOICE_POSITION.INVOICE_ID).in(
                                    select(INVOICE.ID)
                                            .from(INVOICE)
                                            .where(ORGANIZATION.ID.eq(INVOICE.SENDER_ID))
                            ).and(INVOICE_POSITION.PRODUCT_ID.eq(entry.getKey())))).greaterThan(entry.getValue());
                    isFirst = false;
                } else {
                    resultCondition = resultCondition.and(
                            field(select(coalesce(sum(INVOICE_POSITION.AMOUNT), 0L))
                                    .from(INVOICE_POSITION)
                                    .where(row(INVOICE_POSITION.INVOICE_ID).in(
                                            select(INVOICE.ID)
                                                    .from(INVOICE)
                                                    .where(ORGANIZATION.ID.eq(INVOICE.SENDER_ID))
                                    ).and(INVOICE_POSITION.PRODUCT_ID.eq(entry.getKey())))).greaterThan(entry.getValue()));
                }
            }
            return context
                    .selectFrom(ORGANIZATION)
                    .where(resultCondition)
                    .fetchStream()
                    .map(organizationMapper::toEntity)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}

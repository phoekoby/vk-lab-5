package org.example.dao.impl;

import com.google.inject.Inject;
import generated.tables.records.InvoicePositionRecord;
import org.example.config.DBCredentials;
import org.example.dao.InvoicePositionDAO;
import org.example.entity.InvoicePositionDTO;
import org.example.mapper.InvoicePositionMapper;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import static generated.Tables.INVOICE_POSITION;

public class InvoicePositionJooqDAOImpl implements InvoicePositionDAO {

    private final DBCredentials dbCredentials;

    private final InvoicePositionMapper invoicePositionMapper;

    @Inject
    public InvoicePositionJooqDAOImpl(DBCredentials dbCredentials, InvoicePositionMapper invoicePositionMapper) {
        this.dbCredentials = dbCredentials;
        this.invoicePositionMapper = invoicePositionMapper;
    }

    @Override
    public Collection<InvoicePositionDTO> getAll() {
        try (Connection conn = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);

            return context
                    .selectFrom(INVOICE_POSITION)
                    .fetchStream()
                    .map(invoicePositionMapper::toEntity)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<InvoicePositionDTO> getById(@NotNull Long id) {
        try (Connection conn = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);

            final @NotNull Optional<InvoicePositionRecord> record = Optional.ofNullable(context
                    .selectFrom(INVOICE_POSITION)
                    .where(INVOICE_POSITION.ID.eq(id))
                    .fetchOne());
            return record.map(invoicePositionMapper::toEntity);
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
                    .deleteFrom(INVOICE_POSITION)
                    .where(INVOICE_POSITION.ID.eq(id))
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public InvoicePositionDTO update(@NotNull InvoicePositionDTO value) {
        if(value.getId() == null || getById(value.getId()).isEmpty()){
            throw new IllegalArgumentException("Row with this id is not existing");
        }
        try (Connection conn = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);

            context
                    .update(INVOICE_POSITION)
                    .set(INVOICE_POSITION.INVOICE_ID, value.getInvoiceId())
                    .set(INVOICE_POSITION.PRODUCT_ID, value.getProductId())
                    .set(INVOICE_POSITION.AMOUNT, value.getAmount())
                    .set(INVOICE_POSITION.PRICE, value.getPrice())
                    .where(INVOICE_POSITION.ID.eq(value.getId()))
                    .execute();
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public InvoicePositionDTO save(@NotNull InvoicePositionDTO value) {
        try (Connection conn = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);

            context.
                    insertInto(INVOICE_POSITION, INVOICE_POSITION.AMOUNT, INVOICE_POSITION.PRICE, INVOICE_POSITION.PRODUCT_ID, INVOICE_POSITION.INVOICE_ID)
                    .values(value.getAmount(), value.getPrice(), value.getProductId(), value.getInvoiceId())
                    .execute();
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}

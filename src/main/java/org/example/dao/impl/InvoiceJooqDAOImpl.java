package org.example.dao.impl;

import com.google.inject.Inject;
import generated.tables.records.InvoiceRecord;
import org.example.config.DBCredentials;
import org.example.dao.InvoiceDAO;
import org.example.entity.InvoiceDTO;
import org.example.mapper.InvoiceMapper;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import static generated.Tables.INVOICE;

public class InvoiceJooqDAOImpl implements InvoiceDAO {
    private final DBCredentials dbCredentials;

    private final InvoiceMapper invoiceMapper;

    @Inject
    public InvoiceJooqDAOImpl(DBCredentials dbCredentials, InvoiceMapper invoiceMapper) {
        this.dbCredentials = dbCredentials;
        this.invoiceMapper = invoiceMapper;
    }
    @Override
    public Collection<InvoiceDTO> getAll() {
        try (Connection conn = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);

            return context
                    .selectFrom(INVOICE)
                    .fetchStream()
                    .map(invoiceMapper::toEntity)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<InvoiceDTO> getById(@NotNull Long id) {
        try (Connection conn = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);

            final @NotNull Optional<InvoiceRecord> record = Optional.ofNullable(context
                    .selectFrom(INVOICE)
                    .where(INVOICE.ID.eq(id))
                    .fetchOne());
            return record.map(invoiceMapper::toEntity);
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
                    .deleteFrom(INVOICE)
                    .where(INVOICE.ID.eq(id))
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public InvoiceDTO update(@NotNull InvoiceDTO value) {
        if(value.getId() == null || getById(value.getId()).isEmpty()){
            throw new IllegalArgumentException("Row with this id is not existing");
        }
        try (Connection conn = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);

            context
                    .update(INVOICE)
                    .set(INVOICE.INVOICE_DATE, value.getInvoiceDate().toLocalDate())
                    .set(INVOICE.NUMBER, value.getNumber())
                    .set(INVOICE.SENDER_ID, value.getSenderId())
                    .where(INVOICE.ID.eq(value.getId()))
                    .execute();
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public InvoiceDTO save(@NotNull InvoiceDTO value) {
        try (Connection conn = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);

            context.
                    insertInto(INVOICE, INVOICE.NUMBER, INVOICE.INVOICE_DATE, INVOICE.SENDER_ID)
                    .values(value.getNumber(), value.getInvoiceDate().toLocalDate(), value.getSenderId())
                    .execute();
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}

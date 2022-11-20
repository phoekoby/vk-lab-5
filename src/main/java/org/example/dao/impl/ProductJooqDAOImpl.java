package org.example.dao.impl;

import com.google.inject.Inject;
import generated.tables.records.ProductRecord;
import org.example.config.DBCredentials;
import org.example.dao.ProductDAO;
import org.example.dto.AmountSumDTO;
import org.example.dto.PeriodAmountSumAndResultProductDTO;
import org.example.entity.OrganizationDTO;
import org.example.entity.ProductDTO;
import org.example.mapper.ProductMapper;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static generated.Tables.*;
import static org.jooq.impl.DSL.*;

public class ProductJooqDAOImpl implements ProductDAO {


    private final DBCredentials dbCredentials;

    private final ProductMapper productMapper;

    @Inject
    public ProductJooqDAOImpl(DBCredentials dbCredentials, ProductMapper productMapper) {
        this.dbCredentials = dbCredentials;
        this.productMapper = productMapper;
    }

    @Override
    public Collection<ProductDTO> getAll() {
        try (Connection conn = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);

            return context
                    .selectFrom(PRODUCT)
                    .fetchStream()
                    .map(productMapper::toEntity)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<ProductDTO> getById(@NotNull Long id) {
        try (Connection conn = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);

            final @NotNull Optional<ProductRecord> record = Optional.ofNullable(context
                    .selectFrom(PRODUCT)
                    .where(PRODUCT.ID.eq(id))
                    .fetchOne());
            return record.map(productMapper::toEntity);
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
                    .deleteFrom(PRODUCT)
                    .where(PRODUCT.ID.eq(id))
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProductDTO update(@NotNull ProductDTO value) {
        if (value.getId() == null || getById(value.getId()).isEmpty()) {
            throw new IllegalArgumentException("Row with this id is not existing");
        }
        try (Connection conn = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);

            context
                    .update(PRODUCT)
                    .set(PRODUCT.NAME, value.getName())
                    .set(PRODUCT.INTERNAL_CODE, value.getInternalCode())
                    .where(PRODUCT.ID.eq(value.getId()))
                    .execute();
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public ProductDTO save(@NotNull ProductDTO value) {
        try (Connection conn = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);

            context.
                    insertInto(PRODUCT, PRODUCT.NAME, PRODUCT.INTERNAL_CODE)
                    .values(value.getName(), value.getInternalCode())
                    .execute();
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public PeriodAmountSumAndResultProductDTO getAmountAndSumPerDayAndResultForPeriod(Date floor, Date roof) {
        try (Connection conn = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);
            final Map<Date, Map<ProductDTO, AmountSumDTO>> productPerDay = productPerDay(context, floor, roof);
            final Map<ProductDTO, AmountSumDTO> amountSumResultForPeriod = amountSumResultForPeriod(context, floor, roof);
            return new PeriodAmountSumAndResultProductDTO(productPerDay, amountSumResultForPeriod);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<ProductDTO, Double> getAveragePriceForProductsInPeriod(Date floor, Date roof) {
        try (Connection conn = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            final Map<ProductDTO, Double> productWithAveragePrice = new HashMap<>();
            final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);
            context.select(PRODUCT.ID, PRODUCT.NAME, PRODUCT.INTERNAL_CODE, avg(INVOICE_POSITION.PRICE))
                    .from(INVOICE_POSITION)
                    .leftJoin(PRODUCT).on(PRODUCT.ID.eq(INVOICE_POSITION.PRODUCT_ID))
                    .rightJoin(INVOICE).on(INVOICE.ID.eq(INVOICE_POSITION.INVOICE_ID))
                    .where(INVOICE.INVOICE_DATE.between(floor.toLocalDate(), roof.toLocalDate()))
                    .groupBy(PRODUCT.ID)
                    .fetchStream()
                    .forEach(rec -> {
                        productWithAveragePrice.put(new ProductDTO(rec.value1(), rec.value2(), rec.value3()), rec.value4().doubleValue());
                    });
            return productWithAveragePrice;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<OrganizationDTO, List<ProductDTO>> getDeliveredProductsByOrganizationInPeriod(Date floor, Date roof) {
        try (Connection conn = DriverManager.getConnection(dbCredentials.getCONNECTION() + dbCredentials.getDB_NAME(),
                dbCredentials.getUSERNAME(), dbCredentials.getPASSWORD())) {
            final Map<OrganizationDTO, List<ProductDTO>> organizationsWithDeliveredProducts = new TreeMap<>();
            final DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);
            context
                    .select(ORGANIZATION.ID, ORGANIZATION.INN, ORGANIZATION.CHECKING_ACCOUNT, array(
                            select(PRODUCT.ID)
                                    .from(PRODUCT)
                                    .where(PRODUCT.ID.in(
                                            select(INVOICE_POSITION.PRODUCT_ID)
                                                    .from(INVOICE_POSITION)
                                                    .where(INVOICE_POSITION.INVOICE_ID.in(
                                                            select(INVOICE.ID)
                                                                    .from(INVOICE)
                                                                    .where(INVOICE.INVOICE_DATE.between(floor.toLocalDate(), roof.toLocalDate())
                                                                            .and(INVOICE.SENDER_ID.eq(ORGANIZATION.ID)))
                                                    ))
                                    ))
                    ))
                    .from(ORGANIZATION)
                    .fetchStream()
                    .forEach(rec -> organizationsWithDeliveredProducts.put(new OrganizationDTO(rec.value1(), rec.value2(), rec.value3()),
                            Arrays
                                    .stream(rec.value4())
                                    .map(this::getById)
                                    .map(Optional::get)
                                    .collect(Collectors.toList())));
            return organizationsWithDeliveredProducts;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Map<Date, Map<ProductDTO, AmountSumDTO>> productPerDay(@NotNull DSLContext context, Date floor, Date roof) throws SQLException {
        final Map<Date, Map<ProductDTO, AmountSumDTO>> productPerDay = new TreeMap<>();
        context
                .select(INVOICE.INVOICE_DATE, PRODUCT.ID, PRODUCT.NAME, PRODUCT.INTERNAL_CODE, sum(INVOICE_POSITION.AMOUNT),
                        sum(INVOICE_POSITION.AMOUNT.mul(INVOICE_POSITION.PRICE)))
                .from(INVOICE_POSITION)
                .leftJoin(PRODUCT).on(PRODUCT.ID.eq(INVOICE_POSITION.PRODUCT_ID))
                .rightJoin(INVOICE).on(INVOICE.ID.eq(INVOICE_POSITION.INVOICE_ID))
                .where(INVOICE.INVOICE_DATE.between(floor.toLocalDate(), roof.toLocalDate()))
                .groupBy(INVOICE.INVOICE_DATE, PRODUCT.ID)
                .orderBy(INVOICE.INVOICE_DATE)
                .fetchStream()
                .forEach(rec -> {
                            Map<ProductDTO, AmountSumDTO> map = productPerDay
                                    .computeIfAbsent(Date.valueOf(rec.value1()), k -> new HashMap<>());
                            map.put(new ProductDTO(rec.value2(), rec.value3(), rec.value4()),
                                    new AmountSumDTO(rec.value5().intValue(), rec.value6().doubleValue())
                            );
                        }

                );
        return productPerDay;
    }

    private Map<ProductDTO, AmountSumDTO> amountSumResultForPeriod(@NotNull DSLContext context, Date floor, Date roof) throws SQLException {
        final Map<ProductDTO, AmountSumDTO> amountSumResultForPeriod = new HashMap<>();

        context
                .select(PRODUCT.ID, PRODUCT.NAME, PRODUCT.INTERNAL_CODE, sum(INVOICE_POSITION.AMOUNT),
                        sum(INVOICE_POSITION.AMOUNT.mul(INVOICE_POSITION.PRICE)))
                .from(INVOICE_POSITION)
                .leftJoin(PRODUCT).on(PRODUCT.ID.eq(INVOICE_POSITION.PRODUCT_ID))
                .rightJoin(INVOICE).on(INVOICE.ID.eq(INVOICE_POSITION.INVOICE_ID))
                .where(INVOICE.INVOICE_DATE.between(floor.toLocalDate(), roof.toLocalDate()))
                .groupBy(PRODUCT.ID)
                .fetchStream()
                .forEach(rec -> {
                    amountSumResultForPeriod.put(new ProductDTO(rec.value1(), rec.value2(), rec.value3()),
                            new AmountSumDTO(rec.value4().intValue(), rec.value5().doubleValue()));
                });
        return amountSumResultForPeriod;
    }
}

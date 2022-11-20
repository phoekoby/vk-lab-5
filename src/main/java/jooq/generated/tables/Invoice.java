/*
 * This file is generated by jOOQ.
 */
package generated.tables;


import generated.Keys;
import generated.Public;
import generated.tables.records.InvoiceRecord;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Invoice extends TableImpl<InvoiceRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.invoice</code>
     */
    public static final Invoice INVOICE = new Invoice();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<InvoiceRecord> getRecordType() {
        return InvoiceRecord.class;
    }

    /**
     * The column <code>public.invoice.id</code>.
     */
    public final TableField<InvoiceRecord, Long> ID = createField(DSL.name("id"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.invoice.number</code>.
     */
    public final TableField<InvoiceRecord, Long> NUMBER = createField(DSL.name("number"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>public.invoice.invoice_date</code>.
     */
    public final TableField<InvoiceRecord, LocalDate> INVOICE_DATE = createField(DSL.name("invoice_date"), SQLDataType.LOCALDATE.nullable(false), this, "");

    /**
     * The column <code>public.invoice.sender_id</code>.
     */
    public final TableField<InvoiceRecord, Long> SENDER_ID = createField(DSL.name("sender_id"), SQLDataType.BIGINT.nullable(false), this, "");

    private Invoice(Name alias, Table<InvoiceRecord> aliased) {
        this(alias, aliased, null);
    }

    private Invoice(Name alias, Table<InvoiceRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.invoice</code> table reference
     */
    public Invoice(String alias) {
        this(DSL.name(alias), INVOICE);
    }

    /**
     * Create an aliased <code>public.invoice</code> table reference
     */
    public Invoice(Name alias) {
        this(alias, INVOICE);
    }

    /**
     * Create a <code>public.invoice</code> table reference
     */
    public Invoice() {
        this(DSL.name("invoice"), null);
    }

    public <O extends Record> Invoice(Table<O> child, ForeignKey<O, InvoiceRecord> key) {
        super(child, key, INVOICE);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<InvoiceRecord, Long> getIdentity() {
        return (Identity<InvoiceRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<InvoiceRecord> getPrimaryKey() {
        return Keys.INVOICE_PKEY;
    }

    @Override
    public List<ForeignKey<InvoiceRecord, ?>> getReferences() {
        return Arrays.asList(Keys.INVOICE__INVOICE_SENDER_ID_FKEY);
    }

    private transient Organization _organization;

    /**
     * Get the implicit join path to the <code>public.organization</code> table.
     */
    public Organization organization() {
        if (_organization == null)
            _organization = new Organization(this, Keys.INVOICE__INVOICE_SENDER_ID_FKEY);

        return _organization;
    }

    @Override
    public Invoice as(String alias) {
        return new Invoice(DSL.name(alias), this);
    }

    @Override
    public Invoice as(Name alias) {
        return new Invoice(alias, this);
    }

    @Override
    public Invoice as(Table<?> alias) {
        return new Invoice(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Invoice rename(String name) {
        return new Invoice(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Invoice rename(Name name) {
        return new Invoice(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Invoice rename(Table<?> name) {
        return new Invoice(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<Long, Long, LocalDate, Long> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function4<? super Long, ? super Long, ? super LocalDate, ? super Long, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function4<? super Long, ? super Long, ? super LocalDate, ? super Long, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}

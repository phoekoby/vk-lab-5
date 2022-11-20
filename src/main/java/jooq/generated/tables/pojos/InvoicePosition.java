/*
 * This file is generated by jOOQ.
 */
package generated.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class InvoicePosition implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long id;
    private final Double price;
    private final Long productId;
    private final Integer amount;
    private final Long invoiceId;

    public InvoicePosition(InvoicePosition value) {
        this.id = value.id;
        this.price = value.price;
        this.productId = value.productId;
        this.amount = value.amount;
        this.invoiceId = value.invoiceId;
    }

    public InvoicePosition(
        Long id,
        Double price,
        Long productId,
        Integer amount,
        Long invoiceId
    ) {
        this.id = id;
        this.price = price;
        this.productId = productId;
        this.amount = amount;
        this.invoiceId = invoiceId;
    }

    /**
     * Getter for <code>public.invoice_position.id</code>.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Getter for <code>public.invoice_position.price</code>.
     */
    public Double getPrice() {
        return this.price;
    }

    /**
     * Getter for <code>public.invoice_position.product_id</code>.
     */
    public Long getProductId() {
        return this.productId;
    }

    /**
     * Getter for <code>public.invoice_position.amount</code>.
     */
    public Integer getAmount() {
        return this.amount;
    }

    /**
     * Getter for <code>public.invoice_position.invoice_id</code>.
     */
    public Long getInvoiceId() {
        return this.invoiceId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final InvoicePosition other = (InvoicePosition) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        }
        else if (!this.id.equals(other.id))
            return false;
        if (this.price == null) {
            if (other.price != null)
                return false;
        }
        else if (!this.price.equals(other.price))
            return false;
        if (this.productId == null) {
            if (other.productId != null)
                return false;
        }
        else if (!this.productId.equals(other.productId))
            return false;
        if (this.amount == null) {
            if (other.amount != null)
                return false;
        }
        else if (!this.amount.equals(other.amount))
            return false;
        if (this.invoiceId == null) {
            if (other.invoiceId != null)
                return false;
        }
        else if (!this.invoiceId.equals(other.invoiceId))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.price == null) ? 0 : this.price.hashCode());
        result = prime * result + ((this.productId == null) ? 0 : this.productId.hashCode());
        result = prime * result + ((this.amount == null) ? 0 : this.amount.hashCode());
        result = prime * result + ((this.invoiceId == null) ? 0 : this.invoiceId.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("InvoicePosition (");

        sb.append(id);
        sb.append(", ").append(price);
        sb.append(", ").append(productId);
        sb.append(", ").append(amount);
        sb.append(", ").append(invoiceId);

        sb.append(")");
        return sb.toString();
    }
}

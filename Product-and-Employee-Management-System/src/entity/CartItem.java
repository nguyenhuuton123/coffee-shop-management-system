package entity;

import java.io.Serializable;

public class CartItem implements Serializable {

    private int id;

    private final Product product;

    private int quantity;

    private final int unitPrice;

    private int subTotalPrice;

    private static int count;

    public CartItem(Product product, int unitPrice, int quantity, int subTotalPrice) {
        count++;
        this.id = count;
        this.product = product;
        this.quantity = quantity;
        this.subTotalPrice = subTotalPrice;
        this.unitPrice = unitPrice;
    }

    public void setSubTotalPrice(int subTotalPrice) {
        this.subTotalPrice = subTotalPrice;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public int getSubTotalPrice() {
        return subTotalPrice;
    }

    public int getId() {
        return id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BillItem{" +
                "id=" + id +
                ", productId=" + product +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", subTotalPrice=" + subTotalPrice +
                '}';
    }
}

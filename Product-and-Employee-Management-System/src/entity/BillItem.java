package entity;

import java.io.Serializable;

public class BillItem implements Serializable {

    private int id;
    private Product product;

    private int quantity;

    private int unitPrice;

    private int subTotalPrice;

    private static int count;

    public BillItem(Product product, int unitPrice, int quantity, int subTotalPrice) {
        count++;
        this.id = count;
        this.product = product;
        this.quantity = quantity;
        this.subTotalPrice = subTotalPrice;
        this.unitPrice = unitPrice;
    }

    public BillItem() {

    }

    public static void setCount(int count) {
        BillItem.count = count;
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

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setSubTotalPrice(int subTotalPrice) {
        this.subTotalPrice = subTotalPrice;
    }

    public String toString() {
        return product + "-" + unitPrice + "-" + quantity + "-" + subTotalPrice;
    }

}

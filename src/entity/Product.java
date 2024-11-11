package entity;

import java.io.Serializable;

public class Product implements Comparable<Product>, Serializable {

    private String name;
    private int price;
    private int id;

    public Product(String name) {
        this.name = name;
    }

    public Product(int id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public Product() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    public int compareTo(Product o) {
        return (this.getPrice() - o.getPrice());
    }

    @Override
    public String toString() {
        return getId() + "_" + getName() + "_" + getPrice();
    }
}

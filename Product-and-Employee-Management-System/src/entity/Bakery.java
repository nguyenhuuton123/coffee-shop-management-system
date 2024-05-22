package entity;

import java.io.Serializable;

public class Bakery extends Product implements Comparable<Product>, Serializable {

    public Bakery(String name, int price) {
        super(name, price);
    }

    public Bakery(int id, String name, int price) {
        super(id, name, price);
    }

    @Override
    public String toString() {
        return getId() + "_" + getName() + "_" + getPrice();
    }
}

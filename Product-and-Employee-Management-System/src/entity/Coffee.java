package entity;

import java.io.Serializable;

public class Coffee extends Product implements Comparable<Product>, Serializable {

    public Coffee(String name, int price) {
        super(name, price);
    }

    public Coffee(int id, String name, int price) {
        super(id, name, price);
    }

    @Override
    public String toString() {
        return getId() + "_" + getName() + "_" + getPrice();
    }
}
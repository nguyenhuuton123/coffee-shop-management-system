package entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Cart implements Serializable {

    private final String employee;

    private final int totalPrice;

    private List<CartItem> cartItemList;

    private LocalDateTime createdDay;

    public Cart(String employee, int totalPrice, LocalDateTime createdDay, List<CartItem> cartItemList) {
        this.employee = employee;
        this.totalPrice = totalPrice;
        this.createdDay = createdDay;
        this.cartItemList = cartItemList;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public String getEmployee() {
        return employee;
    }

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    public LocalDateTime getCreatedDay() {
        return createdDay;
    }

    public void setCreatedDay(LocalDateTime createdDay) {
        this.createdDay = createdDay;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "employee='" + employee + '\'' +
                ", totalPrice=" + totalPrice +
                ", billItemList=" + cartItemList +
                '}';
    }
}

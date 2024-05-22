package entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Bill implements Serializable {

    private String employeeName;

    private int totalPrice;

    private List<BillItem> billItemList;

    private String createdDay;

    public Bill(String employeeName, int totalPrice, String createdDay, List<BillItem> billItemList) {
        this.employeeName = employeeName;
        this.totalPrice = totalPrice;
        this.createdDay = createdDay;
        this.billItemList = billItemList;
    }

    public Bill() {

    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public String getCreatedDay() {
        return createdDay;
    }

    public void setCreatedDay(String createdDay) {
        this.createdDay = createdDay;
    }

    public void setEmployee(String employee) {
        this.employeeName = employee;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setBillItemList(List<BillItem> billItemList) {
        this.billItemList = billItemList;
    }

    @Override
    public String toString() {
        String billItems = "";
        for (BillItem element : billItemList) {
            billItems += element.toString() + ";";
        }
        return employeeName + "," + totalPrice + "," + createdDay + "," + billItems;
    }
}

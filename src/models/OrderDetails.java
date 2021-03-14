package models;

import java.util.Date;

public class OrderDetails {
    private String id;
    private String name;
    private String address;
    private Date date;
    private String items;
    private double totalPrice;
    private String orderStatus;
    private String customerId;


    public OrderDetails() {
    }

    public OrderDetails(String id, String name, String address, Date date,String items, double totalPrice, String orderStatus, String customerId) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.date = date;
        this.items = items;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.customerId = customerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        items = items;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }


    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}

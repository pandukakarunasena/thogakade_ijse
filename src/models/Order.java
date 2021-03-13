package models;

import java.time.LocalDate;
import java.util.Date;

public class Order {
    private String id;
    private LocalDate date;
    private String customerId;
    private String orderStatus;

    public Order() {
    }

    public Order(String id, LocalDate date, String customerId, String orderStatus) {
        this.id = id;
        this.date = date;
        this.customerId = customerId;
        this.orderStatus = orderStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }


    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", customerId='" + customerId + '\'' +
                '}';
    }
}

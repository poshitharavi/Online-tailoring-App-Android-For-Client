package com.example.tailorar;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {

    int orderId;
    String status;
    Date orderDate;
    String paymentStatus;
    Mesurment mesurment;
    User user;
    Tailor tailor;
    Style style;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Mesurment getMesurment() {
        return mesurment;
    }

    public void setMesurment(Mesurment mesurment) {
        this.mesurment = mesurment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Tailor getTailor() {
        return tailor;
    }

    public void setTailor(Tailor tailor) {
        this.tailor = tailor;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }
}

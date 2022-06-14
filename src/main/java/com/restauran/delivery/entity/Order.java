package com.restauran.delivery.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="allOrders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    int userId;
    boolean isDelivered;
    int year;
    int month;
    int day;

    public Order(int userId, boolean isDelivered) {
        this.userId = userId;
        this.isDelivered = isDelivered;
    }

    public Order(){}

    public Order(int userId, int year, int month, int day) {
        this.userId = userId;
        this.year = year;
        this.month = month;
        this.day = day;
    }
    public void setAll (Order order) {
        this.userId = order.userId;
        this.year = order.year;
        this.month = order.month;
        this.day = order.day;
        this.isDelivered = order.isDelivered;
        this.id = order.id.intValue();
    }
    public Integer getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isDelivered() {
        return isDelivered;
    }

    public void setDelivered(boolean isDelivered) {
        this.isDelivered = isDelivered;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

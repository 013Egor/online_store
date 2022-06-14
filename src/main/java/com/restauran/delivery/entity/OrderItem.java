package com.restauran.delivery.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Integer id;
    
    String name;
    int productId;
    int amount;
    int orderNum;
    int userId;

    public OrderItem(){}
    
    public OrderItem(String name, int productId, int amount) {
        this.name = name;
        this.productId = productId;
        this.amount = amount;
    }

    public OrderItem(String name, int productId, int amount, int orderNum) {
        this.name = name;
        this.productId = productId;
        this.amount = amount;
        this.orderNum = orderNum;
    }

    public void setAll(OrderItem item) {
        this.name = item.name;
        this.productId = item.productId;
        this.amount = item.amount;
        this.orderNum = item.orderNum;
        this.userId = item.userId;
        this.id = item.id.intValue();
    }
    
    public Integer getId() {
        return id;
    }
    
    public int getProductId() {
        return productId;
    }
    
    public void setProductId(int productId) {
        this.productId = productId;
    }
    
    public int getAmount() {
        return amount;
    }
    
    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getOrderNum() {
        return orderNum;
    }
    
    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
